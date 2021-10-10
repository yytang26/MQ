package com.tyy.mq.demo1;

import com.rabbitmq.client.*;
import com.tyy.mq.util.RabbitMqUtil;

/**
 * @author:tyy
 * @date:2021/10/10
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");
        System.out.println("等待接收消息 ......... ");
        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
        // 取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };


        //消费者消费消息
        //1.消费哪个队列
        //2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
        //3.消费者未成功消费的回调
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
