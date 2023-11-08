package com.medhead.POC.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents an hospital document from mongoDB
 */
@Document(collection = "Hospital")
public class Hospital {
    @Id
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private int freeBeds;
    public int getFreeBeds() {
        return freeBeds;
    }

    public void setFreeBeds(int freeBeds) {
        this.freeBeds = freeBeds;
    }

    private String latitude;
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private String longitude;
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String organisationName;
    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    private String[] specialities;

    public String[] getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String[] specialities) {
        this.specialities = specialities;
    }

    public Hospital() {}

    public Hospital(int freeBeds, String latitude, String longitude, String organisationName, String[] specialities) {
        this.freeBeds = freeBeds;
        this.latitude = latitude;
        this.longitude = longitude;
        this.organisationName = organisationName;
        this.specialities = specialities;
    }

    @Override
    public String toString() {
        String str = String.format(
          "{\n    Hospital: {\n        id=%s,\n        freeBeds=%d,\n        latitude=%f,\n        longitude=%f,\n        organisationName=%s,",
          id, freeBeds, latitude, longitude, organisationName);
        str += "\n        specialities: {\n";
        for (String speciality : specialities) {
            str += "            " + speciality + ",\n";
        }
        str = str.substring(0, str.length()-2);
        str += "}\n}";
        return str;
    }
}
