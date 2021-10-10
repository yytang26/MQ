package com.tyy.mq.demo1.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author:tyy
 * @date:2021/10/10 rabbit mq 工具类
 */
public class RabbitMqUtil {

    public static Channel getChannel(String host, String userName, String password) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(userName);
        factory.setPassword(password);
        Connection connection = factory.newConnection();
        return connection.createChannel();

    }
}
