package com.websocket.java.config;

import com.websocket.java.config.db.MongoDbConfiguration;
import com.websocket.java.service.messaging.chat.ChatService;
import com.websocket.java.service.messaging.chat.ChatServiceImpl;
import com.websocket.java.service.messaging.chat.OnMessageProcess;
import com.websocket.java.service.messaging.chat.ProcessMessagesService;
import com.websocket.java.service.messaging.receiving.all.ReceivingDialogsService;
import com.websocket.java.service.messaging.receiving.all.ReceivingService;
import com.websocket.java.service.messaging.receiving.delay.DelayedMessagesService;
import com.websocket.java.service.messaging.receiving.delay.DelayedService;
import com.websocket.java.service.messaging.sending.MessageSessionService;
import com.websocket.java.service.messaging.session.SessionServiceMap;
import com.websocket.java.service.storing.MongoDbStore;
import com.websocket.java.service.storing.StoreService;
import com.websocket.java.service.storing.identity.MongoDbIdGenerator;
import com.websocket.java.util.model.Message;
import com.websocket.java.util.model.MessageDecoder;
import com.websocket.java.util.model.MessageEncoder;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by anton on 29.07.17.
 */
@ServerEndpoint(
        value = "/messaging/{userName}"
        , decoders = MessageDecoder.class
        , encoders = MessageEncoder.class
)
public class ServerEndpointConfiguration {

    private static final SessionServiceMap SESSION_SERVICE = new SessionServiceMap(new HashMap<>());
    //    private static final StoreService STORE_SERVICE = new InMemoryStore(new HashMap<>());
    private static final StoreService STORE_SERVICE = new MongoDbStore(new MongoDbConfiguration(), new MongoDbIdGenerator(new MongoDbConfiguration()));
    private static final ReceivingService RECEIVING_SERVICE = new ReceivingDialogsService(STORE_SERVICE);
    private static final DelayedService DELAYED_SERVICE = new DelayedMessagesService(RECEIVING_SERVICE, new MessageSessionService());
    private static final ChatService CHAT_SERVICE = new ChatServiceImpl(SESSION_SERVICE.getUsers(), new MessageSessionService(), STORE_SERVICE);
    private static final ProcessMessagesService PROCESS_MESSAGES_SERVICE = new OnMessageProcess(CHAT_SERVICE, STORE_SERVICE);

    @OnOpen
    public void onOpenConnection(Session session, @PathParam("userName") String connector) {
        try {
            System.out.println("Opened connection for " + session.getId());
            SESSION_SERVICE.addConnection(session, connector);
            DELAYED_SERVICE.checkDelayed(connector, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessageReceiving(Session session, @PathParam("userName") String username, Message message) {
        try {
            System.out.println("Received message " + message + " from session = " + session.getId());
            message.setArrivedTime(new Timestamp(System.currentTimeMillis()).toString());
            message.setFrom(username);
            PROCESS_MESSAGES_SERVICE.process(message, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed session with id :" + session.getId());
        SESSION_SERVICE.deleteConnection(session);
    }

}
