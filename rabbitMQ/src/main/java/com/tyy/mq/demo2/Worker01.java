package com.tyy.mq.demo2;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tyy.mq.util.RabbitMqUtil;

/**
 * @author:tyy
 * @date:2021/10/10
 */
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String receivedMessage = new String(deliver.getBody());
            System.out.println("接受到的消息：" + receivedMessage);
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("C2消费者启动等待消费");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
