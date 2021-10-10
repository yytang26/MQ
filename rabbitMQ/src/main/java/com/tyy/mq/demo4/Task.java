package com.tyy.mq.demo4;

import com.rabbitmq.client.Channel;
import com.tyy.mq.util.RabbitMqUtil;

import java.util.Scanner;

/**
 * @author:tyy
 * @date:2021/10/10
 */
public class Task {

    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //生成一个队列
        //1.队列名称
        //2.队列里面的消息是否持久化 默认消息存储在内存中
        //3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
        //4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }

}
