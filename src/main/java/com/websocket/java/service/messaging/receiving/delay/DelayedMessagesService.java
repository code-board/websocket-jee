package com.websocket.java.service.messaging.receiving.delay;

import com.websocket.java.service.messaging.receiving.all.ReceivingService;
import com.websocket.java.service.messaging.sending.MessagingService;
import com.websocket.java.util.model.Message;

import javax.websocket.Session;
import java.util.List;

/**
 * Created by anton
 */
public class DelayedMessagesService implements DelayedService<String, List<Message>, Session> {

    private final ReceivingService receivingService;
    private final MessagingService messagingService;

    public DelayedMessagesService(ReceivingService receivingService, MessagingService messagingService) {
        this.receivingService = receivingService;
        this.messagingService = messagingService;
    }

    @Override
    public List<Message> checkDelayed(String delayedReceiver, Session session) {
        List<Message> delayedMessages = (List<Message>) receivingService.receive(delayedReceiver);
        System.out.println("Delayed messages = " + delayedReceiver);

        delayedMessages.forEach(message -> {
            if (!message.getFrom().equals(delayedReceiver))
                messagingService.send(message, session);
        });
        return delayedMessages;
    }
}
