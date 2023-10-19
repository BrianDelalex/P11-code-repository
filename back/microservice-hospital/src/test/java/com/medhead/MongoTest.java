package com.medhead;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.medhead.POC.MhospitalApplication;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {

    private static final String HOST = "localhost";

    private void assertInsertSucceeds(ConfigurableApplicationContext context) {
        System.setProperty("spring.data.mongodb.host", HOST);
        String collectionName = "Hospital";

        MongoTemplate mongo = context.getBean(MongoTemplate.class);
        MongoDatabase db = mongo.getDb();

        MongoCollection<Document> collection = db.getCollection(collectionName);
        FindIterable<Document> it = collection.find();
        for (Object i : it) {
            System.out.println(i);
        }
        assertNotNull(it);
    }

    @Test
    public void whenPropertiesConfig_thenInsertSucceeds() {
        SpringApplicationBuilder app = new SpringApplicationBuilder(MhospitalApplication.class);
        app.run();

        assertInsertSucceeds(app.context());
}
}
