package com.websocket.java.service.messaging.sending;

/**
 * Created by anton
 */
public interface MessagingService<T, S> {

    void send(T message, S session);
}
