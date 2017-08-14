package com.websocket.java.service.messaging.chat;

/**
 * Created by anton
 */
public interface ProcessMessagesService<M, S> {

    void process(M message, S session);
}
