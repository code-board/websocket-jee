package com.websocket.java.service.storing;


import com.websocket.java.config.db.MongoDbConfiguration;
import com.websocket.java.service.storing.identity.MongoDbIdGenerator;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.User;
import org.junit.Test;

import java.util.TreeSet;

/**
 * Created by anton
 */
public class MongoDbStoreTest {

    private static final StoreService storeService = new MongoDbStore(new MongoDbConfiguration(), new MongoDbIdGenerator(new MongoDbConfiguration()));

    @Test
    public void save() {

        Message m = new Message();

        m.setMessage("v");
        m.setTo(new TreeSet<>());
        m.getTo().add(new User("one"));
        m.getTo().add(new User("done"));
        m.getTo().add(new User("2"));

        m.setDeleted(new TreeSet<>());
        m.getDeleted().add(0L);

        storeService.save(m);
    }

    @Test
    public void retrieveByUser() {
        System.out.println(storeService.retrieveByUser("one"));
    }

    @Test
    public void retrieve() {
    }

    @Test
    public void getDialog() {
        Message m = new Message();

        m.setMessage("v");
        m.setTo(new TreeSet<>());
        m.getTo().add(new User("one"));
        m.getTo().add(new User("2"));

        m.setDeleted(new TreeSet<>());
        m.getDeleted().add(4L);

        System.out.println(storeService.getDialog(m.getTo()));
    }

    @Test
    public void updateDialog() {

        Message m = new Message();

        m.setFrom("done");
        m.setIsInDialog(true);
        m.setMessage("v");
        m.setTo(new TreeSet<>());
        m.getTo().add(new User("one"));
        m.getTo().add(new User("done"));
        m.getTo().add(new User("2"));


        storeService.updateInDialog(m);
    }

    @Test
    public void updateRiddenList() {
        Message m = new Message();

        m.setFrom("2");
        m.setMessage("v");
        m.setTo(new TreeSet<>());
        m.getTo().add(new User("one"));
        m.getTo().add(new User("done"));
        m.getTo().add(new User("2"));

        m.setRidden(new TreeSet<>());
        m.getRidden().add(0L);
//        m.getRidden().add(1L);

        storeService.updateRiddenList(m);
    }

    @Test
    public void deleteMessages() {

        Message m = new Message();

        m.setMessage("v");
        m.setTo(new TreeSet<>());
        m.getTo().add(new User("one"));
        m.getTo().add(new User("2"));

        m.setDeleted(new TreeSet<>());
        m.getDeleted().add(3L);

        storeService.deleteMessages(m);

    }


}