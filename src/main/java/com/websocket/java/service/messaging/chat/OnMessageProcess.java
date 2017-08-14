package com.websocket.java.service.messaging.chat;

import com.websocket.java.service.storing.StoreService;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;

import javax.websocket.Session;
import java.util.TreeSet;

/**
 * Created by anton
 */
public class OnMessageProcess implements ProcessMessagesService<Message, Session> {

    private final ChatService chatService;
    private final StoreService storeService;

    public OnMessageProcess(ChatService chatService, StoreService storeService) {
        this.chatService = chatService;
        this.storeService = storeService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(Message message, Session session) {

        // ridden
        if (message.getRidden() != null) {
            if (message.getRiddenBy() == null)
                message.setRiddenBy(new TreeSet<>());
            message.getRiddenBy().add(message.getFrom());
            storeService.updateList(message);
        }

        // deleting
        if (message.getDeleted() == null || message.getDeleted().isEmpty())
            storeService.save(message);
        else
            storeService.deleteMessages(message);

        // is in dialog
        if (message.getTo().size() > 2 & message.getIsInDialog() != null)
            if (message.getTo().contains(new User(message.getFrom())))
                storeService.updateDialog(message);

        chatService.sendMessage(message, session);

    }
}
