package com.medhead.POC.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.medhead.POC.models.Hospital;

/** Utility interface to retrieve Hospital documents from MongoDB.
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String>{
    /**
     * Retrieves all hospitals containing at least one of the given specialities.
     * @param specialities Hospital's specialities you are looking for.
     * @return A List of the hospitals containing at least one of the given specialities.
     */
    public List<Hospital> findBySpecialitiesIsContaining(List<String> specialities);
}
