package com.medhead.POC.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.medhead.POC.models.Speciality;

/** Utility interface to retrieve Speciality documents from MongoDB.
 */
@Repository
public interface SpecialityRepository extends MongoRepository<Speciality, String>{
    
}
