package com.websocket.java.service.messaging.chat;

/**
 * Created by anton
 */
public interface ChatService<M, S> {

    void sendMessage(M message, S session);
}
