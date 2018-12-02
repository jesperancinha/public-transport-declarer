package com.jesperancinha.biscaje.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jesperancinha.biscaje.model.Player;

@Repository
@Transactional
public interface PlayerRepository extends CrudRepository<Player, String> {
	Player findByName(String name);
}
