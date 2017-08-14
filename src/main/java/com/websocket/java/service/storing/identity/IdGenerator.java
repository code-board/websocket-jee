package com.websocket.java.service.storing.identity;

/**
 * Created by anton
 */
public interface IdGenerator<T,V> {

    T generate(V byDialog);
}
