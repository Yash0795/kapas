package com.kapas.user.repository;

import com.kapas.user.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJpaRepository<User, Integer> {

    Optional<User> findUserByUserName(String userName);

    @Query("select u from User u join fetch u.role where u.id = :id")
    User findUserById(@Param("id") Integer id);

    Optional<User> findUserByEmail(String email);
}