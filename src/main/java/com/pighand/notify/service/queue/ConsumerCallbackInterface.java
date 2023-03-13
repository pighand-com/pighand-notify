package com.pighand.notify.service.queue;

/**
 * 消费者处理消息回调函数
 *
 * <p>由QueueService向队列实现类中注入回调方法，具体实现类中消费者监听，获取到消息后，使用回调方法处理消息
 *
 * @author wangshuli
 */
public interface ConsumerCallbackInterface {

    /**
     * 发送消费者消息
     *
     * @param messageId 消息id
     * @param message 消息内容
     */
    void sendConsumerMessage(String messageId, String message);
}
