package com.tyy.mq.demo6.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.tyy.mq.util.RabbitMqUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author:tyy
 * @date:2021/10/11
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "direct";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //创建多个 bindingKey
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("info","普通 info 信息");
        bindingKeyMap.put("warning","警告 warning 信息");
        bindingKeyMap.put("error","错误 error 信息");
        //debug 没有消费这接收这个消息 所有就丢失了
        bindingKeyMap.put("debug","调试 debug 信息");
        for (Map.Entry<String, String> bindingKeyEntry: bindingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, bindingKey, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
