package com.websocket.java.service.messaging.receiving.all;

import com.websocket.java.service.storing.StoreService;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;

import java.util.*;

/**
 * Created by anton
 */
public class ReceivingDialogsService implements ReceivingService<String, List<Message>> {
    private final StoreService storeService;

    public ReceivingDialogsService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public List<Message> receive(String user) {
        Map<Set<User>, List<Message>> response = (Map<Set<User>, List<Message>>) storeService.retrieveByUser(user);
        System.out.println("Dialogs by user = " + response);

        if (!response.isEmpty()) {
            List<Message> allDelayedMessages = new ArrayList<>();
            for (List<Message> messages : response.values()) {
                for (Message m : messages) {
                    if (m.getRiddenBy() == null || !m.getRiddenBy().contains(user))
                        allDelayedMessages.add(m);
                }
            }
            System.out.println("Return delayed messages " + allDelayedMessages);
            return allDelayedMessages;
        } else {
            System.out.println("No delayed messages!");
            return new ArrayList<>();
        }
    }
}
