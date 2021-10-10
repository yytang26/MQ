package com.tyy.mq.demo5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.tyy.mq.util.RabbitMqUtil;

import java.util.Scanner;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author:tyy
 * @date:2021/10/10
 */
public class Provider {

    private final static String QUEUE_NAME = "ack_queue";

    private final static int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //单个发布确认
        confirmIndividual();
        //批量发布确认

        //异步发布确认
    }

    public static void confirmIndividual() throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //生成一个队列
        //1.队列名称
        //2.队列里面的消息是否持久化 默认消息存储在内存中
        //3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
        //4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    public static void confirmBatch() throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //生成一个队列
        //1.队列名称
        //2.队列里面的消息是否持久化 默认消息存储在内存中
        //3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
        //4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        int batch = 100;
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            if (i % (batch + 1) == 0) {
                //服务端返回 false 或超时时间内未返回，生产者可以消息重发
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    public static void confirmAsync() throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //生成一个队列
        //1.队列名称
        //2.队列里面的消息是否持久化 默认消息存储在内存中
        //3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
        //4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         */

        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        /**
         * 确认收到消息的一个回调
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的消息  false 确认当前序列号消息
         */
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                //清除该部分未确认消息
                confirmed.clear();
            } else {
                //只清除当前序列号的消息
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = outstandingConfirms.get(sequenceNumber);
            System.out.println("发布的消息" + message + "未被确认，序列号" + sequenceNumber);
        };

        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback, null);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;

            /**
             *  channel.getNextPublishSeqNo()获取下一个消息的序列号
             *  通过序列号与消息体进行一个关联
             *  全部都是未确认的消息体
             */
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
}
