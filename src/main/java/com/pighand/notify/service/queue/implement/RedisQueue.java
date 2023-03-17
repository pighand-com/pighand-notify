package com.pighand.notify.service.queue.implement;

import com.pighand.notify.service.queue.QueueAbstract;
import com.pighand.notify.service.queue.QueueInterface;
import com.pighand.notify.util.MachineUtil;
import com.pighand.notify.vo.send.SendCommonVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
public class RedisQueue<T extends SendCommonVO> extends QueueAbstract
        implements StreamListener<String, MapRecord<String, String, String>>,
                QueueInterface<T, StreamInfo.XInfoStream> {

    private final String messageKey = "m";

    @Autowired private StringRedisTemplate redisTemplate;

    @Override
    public void createQueue(String queueName) {
        boolean hasKey = redisTemplate.hasKey(queueName);
        if (!hasKey) {
            var emptyRecord =
                    StreamRecords.newRecord()
                            .ofStrings(Map.of(queueName, ""))
                            .withStreamKey(queueName);

            redisTemplate.opsForStream().add(emptyRecord);
            redisTemplate.opsForStream().trim(queueName, 0);
        }
    }

    /**
     * 创建消费者
     *
     * <p>队列不存在，先创建队列
     *
     * <p>如果消费者已存在，不创建
     *
     * @param queueName 队列名
     * @param currentGroupName 组名
     */
    @Override
    public void createConsumerGroup(String queueName, String currentGroupName) {
        this.createQueue(queueName);

        StreamInfo.XInfoGroups groups = redisTemplate.opsForStream().groups(queueName);

        boolean hasGroup = false;
        for (StreamInfo.XInfoGroup group : groups) {
            hasGroup = group.groupName().equals(currentGroupName);

            if (hasGroup) {
                break;
            }
        }

        if (!hasGroup) {
            redisTemplate
                    .opsForStream()
                    .createGroup(queueName, ReadOffset.from("0"), currentGroupName);
        }
    }

    @Override
    public StreamMessageListenerContainer consumerSubscription(
            Object listener,
            String consumerGroupName,
            String queueName,
            ThreadPoolTaskExecutor executor) {
        executor.setThreadNamePrefix(executor.getThreadNamePrefix() + "redis-");

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .batchSize(1)
                        .executor(executor)
                        .build();

        StreamMessageListenerContainer streamMessageListenerContainer =
                StreamMessageListenerContainer.create(
                        redisTemplate.getConnectionFactory(), options);

        streamMessageListenerContainer.receive(
                Consumer.from(consumerGroupName, MachineUtil.getName()),
                StreamOffset.create(queueName, ReadOffset.lastConsumed()),
                this);

        streamMessageListenerContainer.start();

        return streamMessageListenerContainer;
    }

    @Override
    public void pushToQueue(String queueName, String message) {
        Map<String, String> messageMap =
                new HashMap(1) {
                    {
                        put(messageKey, message);
                    }
                };

        redisTemplate.opsForStream().add(queueName, messageMap).getValue();
    }

    @Override
    public void ask(String queueName, String consumerGroupName, String messageId) {
        redisTemplate.opsForStream().delete(queueName, messageId);
    }

    /**
     * 消费者listener 接收消息
     *
     * @param message
     */
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        super.consumerCallbackServices
                .get(message.getStream())
                .sendConsumerMessage(
                        message.getId().getValue(), message.getValue().get(messageKey));
    }
}
