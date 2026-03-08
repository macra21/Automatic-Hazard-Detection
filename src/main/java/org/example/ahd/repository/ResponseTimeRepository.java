package org.example.ahd.repository;

import jakarta.persistence.EntityManager;
import org.example.ahd.domain.ResponseTime;
import org.example.ahd.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResponseTimeRepository extends AbstractRepository<ResponseTime, Integer>{
    public ResponseTimeRepository() {
        super(ResponseTime.class);
    }


    // Not used, frontend problem, because it does not save the logged-in user
    public List<ResponseTime> findByUserAtvUser(Integer userId) {
        try{
            return entityManager.createQuery(
                    "select rt from ResponseTime rt " +
                            "join rt.hazard h " +
                            "where h.atcUser.ID = :userId",
                            ResponseTime.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<ResponseTime> findByCleanupUserId(Integer userId){
        try{
            return entityManager.createQuery(
                            "select rt from ResponseTime rt " +
                                    "join rt.hazard h " +
                                    "where h.cleanupUser.ID = :userId",
                            ResponseTime.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
