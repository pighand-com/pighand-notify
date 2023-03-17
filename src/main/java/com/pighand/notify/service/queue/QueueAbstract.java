package com.pighand.notify.service.queue;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列抽象类
 *
 * @author wangshuli
 */
public class QueueAbstract {

    /** 监听消息中，处理消息的回调接口 */
    protected Map<String, ConsumerCallbackInterface> consumerCallbackServices = new HashMap<>();

    public void consumerCallback(
            String queueName, ConsumerCallbackInterface consumerCallbackService) {
        this.consumerCallbackServices.put(queueName, consumerCallbackService);
    }
}
