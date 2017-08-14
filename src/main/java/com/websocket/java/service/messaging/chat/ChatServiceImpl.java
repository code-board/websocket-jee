package com.websocket.java.service.messaging.chat;

import com.websocket.java.service.messaging.sending.MessagingService;
import com.websocket.java.service.storing.StoreService;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by anton
 */
public class ChatServiceImpl implements ChatService<Message, Session> {

    private final Map<String, List<Session>> users;
    private final MessagingService messagingService;
    private final StoreService storeService;

    public ChatServiceImpl(Map<String, List<Session>> users, MessagingService messagingService, StoreService storeService) {
        this.users = users;
        this.messagingService = messagingService;
        this.storeService = storeService;
    }

    @Override
    public void sendMessage(Message message, Session session) {
        ((TreeSet<User>) storeService.getDialog(message.getTo())).forEach(user -> {
            if (users.containsKey(user.getName())) {
                if (user.getIsInDialog() == null
                        || user.getIsInDialog()
                        || (!user.getIsInDialog() & user.getName().equals(message.getFrom()))
                        )
                    users.get(user.getName()).forEach(s -> {
                        if (s.isOpen() & !s.getId().equals(session.getId()))
                            messagingService.send(message, s);
                        else System.out.println("No open session for :" + message.getTo());
                    });
                else System.out.println("Not in dialog!");
            } else System.out.println("No such receiver!");
        });
    }
}
