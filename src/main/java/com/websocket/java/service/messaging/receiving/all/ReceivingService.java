package com.websocket.java.service.messaging.receiving.all;

/**
 * Created by anton
 */
public interface ReceivingService<T, V> {

    V receive(T user);
}
