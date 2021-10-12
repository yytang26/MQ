package com.tyy.mq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:tyy
 * @date:2021/10/12
 */
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        rabbitTemplate.convertAndSend("X", "XA"
                , "消息来自ttl为10s的队列" + message);

        rabbitTemplate.convertAndSend("X", "XB"
                , "消息来自ttl为40s的队列" + message);
    }
}
