package com.websocket.java.service.storing.identity;

import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by anton
 */
public class PerDialogIdMessageGenerator implements IdGenerator<Long, TreeSet<User>> {

    private final Map<TreeSet<User>, List<Message>> userDialogsMessages;

    public PerDialogIdMessageGenerator(Map<TreeSet<User>, List<Message>> userDialogsMessages) {
        this.userDialogsMessages = userDialogsMessages;
    }

    @Override
    public Long generate(TreeSet<User> byDialog) {
        System.out.println("Generate by : " + byDialog);
        if (userDialogsMessages.isEmpty() && !userDialogsMessages.containsKey(byDialog))
            return 0L;

        if (userDialogsMessages.containsKey(byDialog))
            synchronized (userDialogsMessages.get(byDialog)) {
                List<Message> ms = userDialogsMessages.get(byDialog);
                return ms.isEmpty() ?
                        0L
                        : ms.get(ms.size() - 1).getId() + 1;
            }


        throw new IllegalStateException("Could't generate id!");
    }

}
