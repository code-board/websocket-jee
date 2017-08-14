package com.websocket.java.service.storing;

/**
 * Created by anton
 */
public interface StoreService<T, K, V, D> {

    void save(T data);

    V retrieveByUser(K key);

    T retrieve(K key);

    D getDialog(D dialogName);

    void updateInDialog(T message);

    void updateRiddenList(T message);

    void deleteMessages(T message);
}
