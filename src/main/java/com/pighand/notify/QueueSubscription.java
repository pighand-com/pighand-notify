package com.pighand.notify;

import com.pighand.notify.service.sender.MessageSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 消息订阅
 *
 * @author wangshuli
 */
@Component
public class QueueSubscription {

    @Autowired private MessageSender messageSender;
    //    @Autowired private SmsSender smsSender;

    @Bean
    public StreamMessageListenerContainer message() {
        return (StreamMessageListenerContainer) messageSender.subscription(null);
    }

    //    @Bean
    //    public StreamMessageListenerContainer smsSender() {
    //        return (StreamMessageListenerContainer) smsSender.subscription(null);
    //    }
}
