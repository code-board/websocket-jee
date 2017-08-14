package com.websocket.java.service.storing;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * Created by anton
 */
public class SessionGarbageCollector implements GarbageCollector {

    private final Map<String, List<Session>> users;

    public SessionGarbageCollector(Map<String, List<Session>> users) {
        this.users = users;
    }

    @Override
    public void collect() {
        new Thread(() -> {
            System.out.println("Garbage collect on sessions ^^)");
            synchronized (users) {
                users.forEach((k, v) -> v.removeIf(session -> {
                    if (!session.isOpen()) {
                        System.out.println("Kill session with id " + session.getId());
                        return true;
                    }
                    return false;
                }));
            }
        }).start();
    }

}
