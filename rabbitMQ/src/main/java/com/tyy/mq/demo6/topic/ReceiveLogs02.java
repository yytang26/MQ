package com.tyy.mq.demo6.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tyy.mq.util.RabbitMqUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author:tyy
 * @date:2021/10/11
 */
public class ReceiveLogs02 {

    private static final String EXCHANGE_NAME = "topic";
    private static final String QUEUE_NAME = "Q2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");
        //声明一个临时队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //绑定交换机和队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.*.rabbit*");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息,把接收到的消息打印在屏幕 ........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台打印接收到的消息" + message);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
