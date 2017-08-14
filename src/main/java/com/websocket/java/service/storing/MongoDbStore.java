package com.websocket.java.service.storing;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.result.UpdateResult;
import com.websocket.java.config.db.MongoDbConfiguration;
import com.websocket.java.service.storing.identity.MongoDbIdGenerator;
import com.websocket.java.util.model.Dialog;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.push;
import static java.util.Arrays.asList;

/**
 * Created by anton
 */
public class MongoDbStore implements StoreService<Message, String, Map<TreeSet<User>, List<Message>>, TreeSet<User>> {

    private final MongoDbConfiguration configuration;
    private final MongoDbIdGenerator mongoDbIdGenerator;

    public MongoDbStore(MongoDbConfiguration configuration, MongoDbIdGenerator mongoDbIdGenerator) {
        this.configuration = configuration;
        this.mongoDbIdGenerator = mongoDbIdGenerator;
    }

    @Override
    public void save(Message data) {

        System.out.println("Save message : " + data);
        synchronized (this) {

            Object dialog = configuration.getConnection().getCollection("dialog_messages", Dialog.class)
                    .find(dialogQuery(data))
                    .first();

            data.setId(Long.parseLong(String.valueOf(mongoDbIdGenerator.generate(dialogQuery(data)))));

            if (dialog == null) {
                System.out.println("Create new ");
                TreeSet<User> dialogName = data.getTo();
                List<Message> messages = new ArrayList<>(1);
                messages.add(data);

                Dialog dialog1 = new Dialog();
                dialog1.setDialog(dialogName);
                dialog1.setMessages(messages);
                dialog1.setSeq(0L);

                configuration.getConnection()
                        .getCollection("dialog_messages", Dialog.class)
                        .insertOne(dialog1);
                //bug
                mongoDbIdGenerator.generate(dialogQuery(data));
            } else {
                System.out.println("Update");
                configuration.getConnection()
                        .getCollection("dialog_messages", Dialog.class)
                        .updateOne(dialogQuery(data), combine(push("messages", data)));
            }
        }
    }

    @Override
    public Map<TreeSet<User>, List<Message>> retrieveByUser(String key) {
        System.out.println("Get dialogues by user " + key);
        Map<TreeSet<User>, List<Message>> result = new HashMap<>();
        Block<Dialog> dialog = dialog1 -> result.put(new TreeSet<User>(dialog1.getDialog()), dialog1.getMessages());
        configuration.getConnection().getCollection("dialog_messages", Dialog.class)
                .find(new BasicDBObject("dialog.name", key)).forEach(dialog);
        return result;
    }

    @Override
    public Message retrieve(String key) {
        throw new IllegalStateException("Not specified !");
    }

    @Override
    public TreeSet<User> getDialog(TreeSet<User> dialogName) {
        System.out.println("Get dialog by : " + dialogName);
        List<BasicDBObject> list = new ArrayList<>(dialogName.size());
        dialogName.forEach(
                dialog -> list.add(new BasicDBObject("dialog.name", new BasicDBObject("$in", Collections.singletonList(dialog.getName()))))
        );

        BasicDBObject q = new BasicDBObject();
        q.put("$and", list);

        Dialog dialog = configuration.getConnection().getCollection("dialog_messages", Dialog.class)
                .find(q).first();

        return new TreeSet<>(dialog.getDialog());
    }

    @Override
    public void updateInDialog(Message message) {
        System.out.println("Update is in dialog for message : " + message);

        UpdateResult updateResult = configuration.getConnection().getCollection("dialog_messages", Dialog.class)
                .updateOne(dialogQuery(message).append("$and", Collections.singletonList(new Document("dialog.name", new Document("$in", asList(message.getFrom())))))
                        , new Document("$set", new Document("dialog.$.isInDialog", message.getIsInDialog())));

        System.out.println(updateResult);
    }

    @Override
    public void updateRiddenList(Message message) {
        System.out.println("Update ridden by for message : " + message);
        UpdateResult updateResult = configuration.getConnection()
                .getCollection("dialog_messages", Message.class)
                .updateMany(dialogQuery(message).append("messages._id", new BasicDBObject("$in", message.getRidden())),
                        new Document("$push", new Document("messages.$.riddenBy", message.getFrom())));
        System.out.println(updateResult);
    }

    @Override
    public void deleteMessages(Message message) {
        System.out.println("Delete message : " + message);
        BasicDBObject inQuery = new BasicDBObject();
        inQuery.put("messages", new BasicDBObject("_id", new BasicDBObject("$in", message.getDeleted())));

        BasicDBObject update = new BasicDBObject();
        update.put("$pull", inQuery);

        configuration.getConnection().getCollection("dialog_messages", Message.class)
                .updateOne(dialogQuery(message), update);
    }

    private BasicDBObject dialogQuery(Message message) {
        List<BasicDBObject> list = new ArrayList<>(message.getTo().size());
        message.getTo().forEach(
                dialog -> list.add(new BasicDBObject("dialog.name", new BasicDBObject("$in", Collections.singletonList(dialog.getName()))))
        );
        BasicDBObject dialogQuery = new BasicDBObject();
        dialogQuery.put("$and", list);
        return dialogQuery;
    }
}
