package com.jesperancinha.biscaje.service;

import com.jesperancinha.biscaje.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface PlayerRepository extends CrudRepository<Player, String> {
}

