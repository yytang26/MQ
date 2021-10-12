package com.tyy.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:tyy
 * @date:2021/10/12 rabbit mq配置类
 */
@Configuration
public class RabbitMqConfig {

    @Bean("exChangeX")
    public DirectExchange exChangeX() {
        return new DirectExchange("X");
    }

    @Bean("exChangeY")
    public DirectExchange exChangeY() {
        return new DirectExchange("Y");
    }

    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "Y");
        map.put("x-dead-letter-routing-key", "YD");
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable("QA").withArguments(map).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("x-dead-letter-exchange", "Y");
        map.put("x-dead-letter-routing-key", "YD");
        map.put("x-message-ttl", 40000);
        return QueueBuilder.durable("QB").withArguments(map).build();
    }

    @Bean("queueD")
    public Queue queueD() {
        return new Queue("QD");
    }

    // 声明队列 A 绑定 X 交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("exChangeX")
            DirectExchange exChangeX) {
        return BindingBuilder.bind(queueA).to(exChangeX).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("exChangeX")
            DirectExchange exChangeX) {
        return BindingBuilder.bind(queueB).to(exChangeX).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("exChangeY")
            DirectExchange exChangeX) {
        return BindingBuilder.bind(queueD).to(exChangeX).with("YD");
    }


    //声明队列 C 死信交换机
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(4);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", "Y");
        //声明当前队列的死信路由
        args.put("x-dead-letter-routing-key", "YD");
        //没有声明 TTL 属性
        return QueueBuilder.durable("queueC").withArguments(args).build();
    }

    //声明队列 B 绑定 X 交换机
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("exChangeX") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


}
