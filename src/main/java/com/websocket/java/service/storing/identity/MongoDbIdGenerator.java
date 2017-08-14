package com.websocket.java.service.storing.identity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.websocket.java.config.db.MongoDbConfiguration;

/**
 * Created by anton
 */
public class MongoDbIdGenerator implements IdGenerator<Object, BasicDBObject> {

    private final MongoDbConfiguration configuration;

    public MongoDbIdGenerator(MongoDbConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object generate(BasicDBObject dialog) {
        DBCollection collection = configuration.getMongoClient().getDB(configuration.getConnection().getName())
                .getCollection("dialog_messages");
        BasicDBObject update = new BasicDBObject();
        update.put("$inc", new BasicDBObject("seq", 1));
        DBObject obj = collection.findAndModify(dialog, update);

        return obj == null ? 0 : obj.get("seq");
    }
}
