package com.websocket.java.service.messaging.session;

/**
 * Created by anton
 */
public interface SessionService<K1, K2> {

    void addConnection(K2 session, K1 user);

    void deleteConnection(K2 session);

}
