package com.yucei.admin.common.jms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

/**
 * 启用activemq
 * 注意引入包 spring-boot-starter-activemq 与activemq-pool
 */
@Configuration
@EnableJms
public class ActivemqConfig {

    @Bean("jmsQueueListenerContainerFactory")
    public JmsListenerContainerFactory jmsQueueListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置连接数
        factory.setConcurrency("3-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setPubSubDomain(false);
        return factory;

    }

    @Bean("jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //重连间隔时间
        factory.setPubSubDomain(true);
        return factory;

    }
}
