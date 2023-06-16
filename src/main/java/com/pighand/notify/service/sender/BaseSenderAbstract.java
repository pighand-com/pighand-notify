package com.pighand.notify.service.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.service.queue.QueueInterface;
import com.pighand.notify.vo.send.SendCommonVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 各个消息子类，需继承当前类。
 *
 * <p>泛型：T 消息内容实体；Q 消息队列对象
 *
 * <p>1. 子类实现send方法。父类监听到消费消息，调用子类send方法发送消息，并自动ask（ask逻辑在队里实现类中）
 *
 * <p>2. 如果启用消息队列，异步发送时，调用此类的push方法
 *
 * <p>3. Application中使用以下方法启用消费者监听：
 *
 *
 * <blockquote>
 *     * redis stream listener
 *     <pre>
 *     {@code @Autowired} private QueueService queueService;
 *
 *     {@code @bean}
 *     public Subscription redisQueue(StreamMessageListenerContainer listener) {
 *          return (Subscription)queueService.subscription(listener);
 *     }
 *     <pre>
 * <blockquote/>
 *
 * @author wangshuli
 */
@Slf4j
@Component
public abstract class BaseSenderAbstract<T extends SendCommonVO> {
    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    private final Class<T> clz;
    private final ObjectMapper om = new ObjectMapper();
    private final String queueName;

    @Value("${pighand.notify.async-sender-pool.core: 1}")
    private Integer asyncSenderPoolCore;

    @Value("${pighand.notify.async-sender-pool.max: 4}")
    private Integer asyncSenderPoolMax;

    @Value("${pighand.notify.async-sender-pool.capacity: 100}")
    private Integer asyncSenderPoolCapacity;

    @Value("${pighand.notify.async-sender-pool.keep-alive: 60}")
    private Integer asyncSenderPoolKeepAlive;

    @Value("${pighand.notify.async-sender-pool.thread-name: async-sender-}")
    private String asyncSenderPoolThreadName;

    @Value("${pighand.notify.queue: #{null}}")
    private String configQueueName;

    private Boolean isEnabledQueue = false;
    private QueueInterface<T, Object> queue;
    private String consumerGroupName = "default";

    public BaseSenderAbstract() {
        Type type = getClass().getGenericSuperclass();
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
        Class typeClass = ((Class) argument);

        String typeName = typeClass.getName();
        String packageName = typeClass.getPackageName();

        this.queueName = String.format("queue%s", typeName.replace(packageName, ""));
        this.consumerGroupName = String.format("consumer%s", typeName.replace(packageName, ""));
        this.clz = (Class<T>) argument;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 设置异步执行线程池
        executor.setCorePoolSize(this.asyncSenderPoolCore);
        executor.setMaxPoolSize(this.asyncSenderPoolMax);
        executor.setQueueCapacity(this.asyncSenderPoolCapacity);
        executor.setKeepAliveSeconds(this.asyncSenderPoolKeepAlive);
        executor.setThreadNamePrefix(this.asyncSenderPoolThreadName);
        executor.initialize();

        // 启用队列
        if (StringUtils.hasText(configQueueName)) {
            this.queue = (QueueInterface<T, Object>) applicationContext.getBean(configQueueName);

            // 向消息队列实现类中，注入处理消费者消息的回调函数
            this.queue.consumerCallback(
                    this.queueName,
                    (messageId, message) -> sendConsumerMessage(messageId, message));

            isEnabledQueue = true;
        }
    }

    /**
     * 序列化要发送的消息
     *
     * @param vo
     * @return string
     */
    private String messageToJson(T vo) {
        try {
            return om.writeValueAsString(vo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反要发送的消息
     *
     * @param message
     * @return
     */
    private T messageFromJson(String message) {
        try {
            return om.readValue(message, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 消息订阅
     *
     * @param listener
     * @return
     */
    public Object subscription(Object listener) {
        queue.createConsumerGroup(queueName, consumerGroupName);

        return queue.consumerSubscription(listener, consumerGroupName, queueName, executor);
    }

    /**
     * 向队列推送消息
     *
     * @param vo
     */
    public void push(T vo) {
        // 队列不存在则创建
        queue.createQueue(queueName);

        // 向队列推送消息
        String message = messageToJson(vo);
        queue.pushToQueue(queueName, message);
    }

    /**
     * 发送消费者消息，消费成功后ask
     *
     * @param message
     */
    private void sendConsumerMessage(String messageId, String message) {
        T messageObject = messageFromJson(message);

        try {
            send(messageObject);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        queue.ask(queueName, consumerGroupName, messageId);
    }

    /**
     * 异步发送消息
     *
     * <p>1. 判断子类是否实现异步。如果有，使用异步实现发送
     *
     * <p>2. 判断是否启用队列。如果启用，推送至队列
     *
     * <p>3. 调用异步发送类
     *
     * @param message
     * @return
     * @throws Exception
     */
    public Map<EnumTemplateParams, String> sendAsync(T message) throws Exception {
        message.setIsFormatSendContents(true);

        Map<EnumTemplateParams, String> returnParams = this.replaceSendContent(message);

        // 内部异步实现发送
        Boolean isHasSendAsync = internalSendAsync(message);

        if (isHasSendAsync) {
            return returnParams;
        }

        if (this.isEnabledQueue) {
            // 消息队列发送
            this.push(message);
        } else {
            // 异步线程发送消息
            executor.execute(
                    () -> {
                        try {
                            this.send(message);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    });
        }

        return returnParams;
    }

    /**
     * 同步发送消息
     *
     * @param message
     * @return
     * @throws Exception
     */
    public Map<EnumTemplateParams, String> send(T message) throws Exception {
        Map<EnumTemplateParams, String> returnParams = null;
        if (message.getIsFormatSendContents() == null || !message.getIsFormatSendContents()) {
            returnParams = this.replaceSendContent(message);
        }

        this.internalSend(message);

        return returnParams;
    }

    /**
     * 替换模板参数
     *
     * @param message
     * @return
     * @throws Exception
     */
    protected abstract Map<EnumTemplateParams, String> replaceSendContent(T message)
            throws Exception;

    /**
     * 异步发送具体实现
     *
     * @param message
     * @return false 无异步实现
     * @throws Exception
     */
    protected abstract Boolean internalSendAsync(T message) throws Exception;

    /**
     * 同步发送具体实现
     *
     * @param message
     * @throws Exception
     */
    protected abstract void internalSend(T message) throws Exception;
}
