package com.example.repository;

import com.example.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query("{ 'email': ?0 }")
    @Update("{ '$set': { 'password' : ?1} }")
    void changePassword(String email, String password);
}
