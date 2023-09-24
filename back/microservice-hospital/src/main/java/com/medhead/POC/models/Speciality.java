package com.medhead.POC.models;

import java.text.Format;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "specialities")
public class Speciality {
    @Id
    public String id;

    public String group;
    public String speciality;

    public String ToJson() {
        return String.format("{ \"speciality\": \"%s\", \"group\": \"%s\" }", speciality, group);
    }
}
