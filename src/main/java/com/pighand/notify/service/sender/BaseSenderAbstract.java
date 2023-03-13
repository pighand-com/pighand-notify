package com.pighand.notify.service.sender;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.service.queue.QueueInterface;
import com.pighand.notify.vo.send.SendCommonVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
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

    @Value("${pighand.notify.queue: #{null}}")
    private String configQueueName;

    private Boolean isEnabledQueue = false;

    private QueueInterface<T, Object> queue;

    private Class<T> clz;

    private Gson gson = new Gson();

    private String queueName;

    private String consumerGroupName = "default";

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (StringUtils.hasText(configQueueName)) {
            this.queue = (QueueInterface<T, Object>) applicationContext.getBean(configQueueName);

            // 向消息队列实现类中，注入处理消费者消息的回调函数
            queue.consumerCallback((messageId, message) -> sendConsumerMessage(messageId, message));

            isEnabledQueue = true;
        }
    }

    public BaseSenderAbstract() {
        Type type = getClass().getGenericSuperclass();
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];

        this.queueName = argument.getTypeName();
        this.clz = (Class<T>) argument;
    }

    /**
     * 序列化要发送的消息
     *
     * @param vo
     * @return string
     */
    private String messageToJson(T vo) {
        JsonElement json = gson.toJsonTree(vo);
        return json.toString();
    }

    /**
     * 反要发送的消息
     *
     * @param message
     * @return
     */
    private T messageFromJson(String message) {
        return (T) gson.fromJson(message, clz.getGenericSuperclass());
    }

    /**
     * 消息订阅
     *
     * @param listener
     * @return
     */
    public Object subscription(Object listener) {
        queue.createConsumerGroup(queueName, consumerGroupName);

        return queue.consumerSubscription(listener, consumerGroupName, queueName);
    }

    /**
     * 向队列推送消息
     *
     * @param vo
     */
    public void push(T vo) {
        // 队列不存在则创建
        Object queueObject = queue.getQueue(queueName);
        if (queueObject == null) {
            queue.createQueue(queueName);
        }

        // 向队列推送消息
        String message = messageToJson(vo);
        queue.pushToQueue(queueObject, queueName, message);
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
    public Map<EnumTemplateParams, Object> sendAsync(T message) throws Exception {
        message.setIsFormatSendContents(true);

        Map<EnumTemplateParams, Object> returnParams = this.replaceSendContent(message);

        Boolean isHasSendAsync = internalSendAsync(message);

        if (isHasSendAsync) {
            return returnParams;
        }

        if (this.isEnabledQueue) {
            this.push(message);
        } else {
            this._sendAsync(message);
        }

        return returnParams;
    }

    @Async
    void _sendAsync(T message) throws Exception {
        this.send(message);
    }

    /**
     * 同步发送消息
     *
     * @param message
     * @return
     * @throws Exception
     */
    public Map<EnumTemplateParams, Object> send(T message) throws Exception {
        Map<EnumTemplateParams, Object> returnParams = null;
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
    protected abstract Map<EnumTemplateParams, Object> replaceSendContent(T message)
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
