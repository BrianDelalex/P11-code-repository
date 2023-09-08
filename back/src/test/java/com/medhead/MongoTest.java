package com.medhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.medhead.POC.PocApplication;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {
    private static final String HOST = "localhost";
    private static final String PORT = "27017";
    private static final String DB = "P11";

    private void assertInsertSucceeds(ConfigurableApplicationContext context) {
        String name = "Hospital";

        MongoTemplate mongo = context.getBean(MongoTemplate.class);
        MongoDatabase db = mongo.getDb();

        MongoCollection collection = db.getCollection("Hospital");
        FindIterable it = collection.find();
        for (Object i : it) {
            System.out.println(i);
        }
        assertNotNull(it);
    }

    @Test
    public void whenPropertiesConfig_thenInsertSucceeds() {
        SpringApplicationBuilder app = new SpringApplicationBuilder(PocApplication.class);
        app.run();

        assertInsertSucceeds(app.context());
}
}
