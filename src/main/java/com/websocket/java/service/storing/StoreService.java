package com.websocket.java.service.storing;

/**
 * Created by anton
 */
public interface StoreService<T, K, V, D> {

    void save(T data);

    V retrieveByUser(K key);

    T retrieve(K key);

    D getDialog(D dialogName);

    void updateDialog(T message);

    void updateList(T message);

    void deleteMessages(T message);
}
