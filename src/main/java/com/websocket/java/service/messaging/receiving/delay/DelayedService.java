package com.websocket.java.service.messaging.receiving.delay;

import java.util.Collection;

/**
 * Created by anton
 */
public interface DelayedService<K, C extends Collection<?>, S> {

    C checkDelayed(K delayedReceiver, S session);
}
