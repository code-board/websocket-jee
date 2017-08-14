package com.websocket.java.service.storing;

import com.websocket.java.service.storing.identity.IdGenerator;
import com.websocket.java.service.storing.identity.PerDialogIdMessageGenerator;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by anton
 */
public class InMemoryStore implements StoreService<Message, String, Map<TreeSet<User>, List<Message>>, TreeSet<User>> {

    private final Map<TreeSet<User>, List<Message>> dialogsMessages;
    private final IdGenerator generator;

    public InMemoryStore(
            Map<TreeSet<User>, List<Message>> dialogsMessages
    ) {
        this.dialogsMessages = dialogsMessages;
        this.generator = new PerDialogIdMessageGenerator(dialogsMessages);
    }

    @Override
    public void save(Message data) {
        System.out.println("Saving data :" + data);
        synchronized (dialogsMessages) {

            data.setId((Long) generator.generate(data.getTo()));

            if (dialogsMessages.isEmpty() && !dialogsMessages.containsKey(data.getTo())) {

                TreeSet<User> newDialog = new TreeSet<>();
                newDialog.addAll(data.getTo());
                List<Message> newMs = new ArrayList<>();
                newMs.add(data);

                dialogsMessages.put(newDialog, newMs);
            } else
                dialogsMessages.get(data.getTo()).add(data);
        }
    }

    @Override
    public Map<TreeSet<User>, List<Message>> retrieveByUser(String key) {
        User keyUser = new User(key);
        return this.dialogsMessages
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().contains(keyUser))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Message retrieve(String key) {
        throw new IllegalStateException("No realization yet!");
    }

    @Override
    public TreeSet<User> getDialog(TreeSet<User> dialogName) {
        return dialogsMessages.keySet().stream().filter(set -> set.equals(dialogName)).findFirst().get();
    }

    @Override
    public void updateInDialog(Message message) {
        System.out.println("Update in dialog : " + message);
        dialogsMessages.keySet().forEach(setKey -> {
            if (setKey.equals(message.getTo()))
                synchronized (dialogsMessages.keySet()) {
                    setKey.forEach(user -> {
                        if (user.getName().equals(message.getFrom()))
                            user.setIsInDialog(message.getIsInDialog());
                    });
                }
        });
    }

    @Override
    public void updateRiddenList(Message message) {
        synchronized (dialogsMessages.get(message.getTo())) {
            for (int i = 0; i < dialogsMessages.get(message.getTo()).size(); i++) {
                if (message.getRidden().contains(dialogsMessages.get(message.getTo()).get(i).getId()))

                    if (dialogsMessages.get(message.getTo()).get(i).getRiddenBy() == null)
                        dialogsMessages.get(message.getTo()).get(i).setRiddenBy(new TreeSet<>());

                dialogsMessages.get(message.getTo()).get(i).getRiddenBy().add(message.getFrom());
            }
        }
    }

    @Override
    public void deleteMessages(Message message) {
        if (!dialogsMessages.isEmpty()
                & dialogsMessages.get(message.getTo()) != null
                && !dialogsMessages.get(message.getTo()).isEmpty())
            new ArrayList<>(dialogsMessages.get(message.getTo())).forEach(m -> {
                if (message.getDeleted().contains(m.getId()) & m.getFrom().equals(message.getFrom()))
                    synchronized (dialogsMessages.get(message.getTo())) {
                        dialogsMessages.get(message.getTo()).remove(m);
                    }
            });
    }

}
