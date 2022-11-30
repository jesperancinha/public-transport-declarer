package com.jesperancinha.biscaje.service;

import com.jesperancinha.biscaje.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
