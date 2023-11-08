package com.medhead.POC.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents a speciality document from mongoDB
 */
@Document(collection = "specialities")
public class Speciality {
    @Id
    public String id;

    public String group;
    public String speciality;

    /** Serialize the Speciality object into JSON a string.
     * 
     * @return String containing the object data into a JSON formatted string.
     */
    public String ToJson() {
        return String.format("{ \"speciality\": \"%s\", \"group\": \"%s\" }", speciality, group);
    }
}
