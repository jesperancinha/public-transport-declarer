package com.steelzack.biscaje.service;

import com.steelzack.biscaje.entities.User;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface BiscaService {
    EntityManager getEntityManager();

    boolean updateUser(User user);

    boolean deleteUser(User user);

    boolean  createUser(String joao, String s, Date date);
}
