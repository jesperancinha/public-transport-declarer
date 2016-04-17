package com.steelzack.biscaje.service;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@ApplicationScoped
public class BiscaService {

    private EntityManager entityManager;

    public BiscaService() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("biscaje-pu");
        entityManager = factory.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
