package com.medhead.POC.models;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String>{
    public List<Hospital> findBySpecialitiesIsContaining(List<String> specialities);
    public List<Hospital> findByFreeBeds(int freeBeds);
}
