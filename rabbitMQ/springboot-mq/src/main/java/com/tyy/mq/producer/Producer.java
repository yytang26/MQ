package com.tyy.mq.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author:tyy
 * @date:2021/10/13
 */
@RestController
@RequestMapping("/confirm")
public class Producer {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyCallBack myCallBack;

    //依赖注入rabbitmq之后，再设置回调对象
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(myCallBack);
    }

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        //指定消息id为1
        CorrelationData correlationData = new CorrelationData("1");
        String routingKey = "key1";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData);

        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData2);

    }
}
