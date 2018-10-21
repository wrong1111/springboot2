package com.yucei.admin.common.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 点到点的接收
 */
@Slf4j
@Component
public class QueueReceiver {
    @JmsListener(destination = "queue", containerFactory = "jmsQueueListenerContainerFactory")
    public void receive(String msg) {
        log.debug("====queue 监听到的消息内容为: " + msg);
    }

}
