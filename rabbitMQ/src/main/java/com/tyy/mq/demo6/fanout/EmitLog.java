package com.tyy.mq.demo6.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tyy.mq.util.RabbitMqUtil;

import java.util.Scanner;

/**
 * @author:tyy
 * @date:2021/10/11
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner sc= new Scanner(System.in);
        while (sc.hasNext()) {
            String s = sc.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,s.getBytes("UTF-8"));
        }
    }
}
