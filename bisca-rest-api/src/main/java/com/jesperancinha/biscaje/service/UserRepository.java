package com.jesperancinha.biscaje.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jesperancinha.biscaje.model.Player;
import com.jesperancinha.biscaje.model.User;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
	User findByName(String name);
}
