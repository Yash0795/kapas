package com.kapas.user.repository;

import com.kapas.user.entity.Sessions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionsRepository extends CrudRepository<Sessions, Integer> {

    @Query("delete from Sessions s where s.expiresAt <= now()")
    @Modifying
    void deleteAllExpiredSessions();

    @Query("select s from Sessions s join fetch s.user u where u.id = :userId and s.sessionId = :sessionId")
    Optional<Sessions> findByUserIdAndSessionId(Integer userId, String sessionId);
}
