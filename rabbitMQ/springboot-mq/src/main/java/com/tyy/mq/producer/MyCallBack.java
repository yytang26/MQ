package com.tyy.mq.producer;

;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author:tyy
 * @date:2021/10/13
 */
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? "" : correlationData.getId();
        if (ack) {
            System.out.println("交换机已经收到id是" + id + "的消息了");
        } else {
            System.out.println("交换机没有收到id是" + id + "的消息了");
        }
    }
}
