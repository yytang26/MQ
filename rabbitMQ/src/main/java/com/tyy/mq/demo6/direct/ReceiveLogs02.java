package com.tyy.mq.demo6.direct;

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

    private static final String EXCHANGE_NAME = "direct";
    private static final String QUEUE_NAME = "disk";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("127.0.0.1", "tyy", "123");
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //绑定交换机和队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        System.out.println("等待接收消息,把接收到的消息写入文件 ........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            File file = new File("D://1.txt");
            FileUtils.writeStringToFile(file,message,"UTF-8");
            System.out.println("写入文件成功"+message);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
