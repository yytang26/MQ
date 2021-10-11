package com.tyy.mq.demo6.fanout;

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

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");
        //声明一个临时队列
        String queue = channel.queueDeclare().getQueue();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //绑定交换机和队列
        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("等待接收消息,把接收到的消息写道文件 ........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            File file = new File("D://1.txt");
            FileUtils.writeStringToFile(file,message,"UTF-8");
            System.out.println("写入文件成功"+message);
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });
    }
}
