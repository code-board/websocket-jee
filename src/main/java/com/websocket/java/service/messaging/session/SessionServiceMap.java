package com.websocket.java.service.messaging.session;

import lombok.Getter;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anton
 */
@Getter
public class SessionServiceMap implements SessionService<String, Session> {

    private final Map<String, List<Session>> users;

    public SessionServiceMap(Map<String, List<Session>> users) {
        this.users = users;
    }

    @Override
    public void addConnection(Session session, String user) {
        if (users.containsKey(user))
            users.get(user).add(session);
        else {
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            users.put(user, sessions);
        }
    }

    @Override
    public void deleteConnection(Session session) {
        System.out.println("Delete session : " + session);
        users.values().remove(session);
    }
}
