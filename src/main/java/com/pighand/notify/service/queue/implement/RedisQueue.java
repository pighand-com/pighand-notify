package com.pighand.notify.service.queue.implement;

import com.pighand.notify.service.queue.ConsumerCallbackInterface;
import com.pighand.notify.service.queue.QueueInterface;
import com.pighand.notify.util.MachineUtil;
import com.pighand.notify.vo.send.SendCommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * redis消息队列 具体实现
 *
 * <p>T 消息队列实体
 *
 * <p>StreamListener 消费者监听接口
 *
 * <p>QueueInterface 消费者统一接口
 *
 * @author wangshuli
 */
@Component("redisQueue")
public class RedisQueue<T extends SendCommonVO>
        implements StreamListener<String, MapRecord<String, String, String>>,
                QueueInterface<T, StreamInfo.XInfoStream> {

    private final String messageKey = "m";

    private ConsumerCallbackInterface consumerCallbackService;

    @Autowired private StringRedisTemplate redisTemplate;

    /**
     * redis在push消息时，自动创建。此处不用检测队列是否存在
     *
     * @param queueName
     * @return
     */
    @Override
    public StreamInfo.XInfoStream getQueue(String queueName) {
        return null;
    }

    @Override
    public void createQueue(String queueName) {}

    @Override
    public void createConsumerGroup(String queueName, String currentGroupName) {
        redisTemplate.opsForStream().createGroup(queueName, currentGroupName);
    }

    @Override
    public Subscription consumerSubscription(
            Object listener, String consumerGroupName, String queueName) {
        Subscription subscription =
                ((StreamMessageListenerContainer) listener)
                        .receive(
                                Consumer.from(consumerGroupName, MachineUtil.getName()),
                                StreamOffset.create(queueName, ReadOffset.lastConsumed()),
                                this);

        return subscription;
    }

    @Override
    public void pushToQueue(StreamInfo.XInfoStream queue, String queueName, String message) {
        Map<String, String> messageMap =
                new HashMap(1) {
                    {
                        put(messageKey, "message");
                    }
                };

        redisTemplate.opsForStream().add(queueName, messageMap).getValue();
    }

    @Override
    public void ask(String queueName, String consumerGroupName, String messageId) {
        redisTemplate
                .opsForStream()
                .acknowledge(queueName, consumerGroupName, RecordId.of(messageId));
    }

    @Override
    public void consumerCallback(ConsumerCallbackInterface consumerCallbackService) {
        this.consumerCallbackService = consumerCallbackService;
    }

    /**
     * 消费者listener 接收消息
     *
     * @param message
     */
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        this.consumerCallbackService.sendConsumerMessage(
                message.getId().getValue(), message.getValue().get(messageKey));
    }
}
