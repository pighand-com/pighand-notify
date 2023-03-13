package com.pighand.notify.service.queue;

/**
 * 消费者统一接口
 *
 * <p>新添加的队列实现，均需实现此接口
 *
 * @author wangshuli
 */
public interface QueueInterface<T, Q> {

    /**
     * 获取消息队列对象
     *
     * @param queueName
     * @return queue object
     */
    Q getQueue(String queueName);

    /**
     * 创建消息队列
     *
     * @param queueName
     */
    void createQueue(String queueName);

    /**
     * 创建消费组
     *
     * @param queueName 队列名
     * @param currentGroupName 组名
     */
    void createConsumerGroup(String queueName, String currentGroupName);

    /**
     * 消息订阅实现
     *
     * @param listener
     * @param consumerGroupName
     * @param queueName
     * @return
     */
    Object consumerSubscription(Object listener, String consumerGroupName, String queueName);

    /**
     * 向消息队列推送消息
     *
     * @param queue 队列对象
     * @param queueName 队列名
     * @param message
     * @return message id
     */
    void pushToQueue(Q queue, String queueName, String message);

    /**
     * 消费应答
     *
     * @param queueName
     * @param consumerGroupName
     * @param messageId
     */
    void ask(String queueName, String consumerGroupName, String messageId);

    /**
     * 处理监听返回的消息
     *
     * @param method
     */
    void consumerCallback(ConsumerCallbackInterface method);
}
