package com.example.cassandra.repository;

import com.example.cassandra.repository.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CassandraRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE username=?0 ALLOW FILTERING")
    Optional<User> findByUsername(String username);
}