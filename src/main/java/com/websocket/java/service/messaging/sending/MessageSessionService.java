package com.websocket.java.service.messaging.sending;

import com.websocket.java.util.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by anton
 */
public class MessageSessionService implements MessagingService<Message, Session> {

    @Override
    public void send(Message message, Session session) {
        System.out.println("Send message : " + message + " to " + session);
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
