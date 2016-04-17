package com.steelzack.biscaje.service;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@ApplicationScoped
public class BiscaService {

    @PersistenceContext(unitName = "biscaje-pu", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if(entityManager == null)
        {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("biscaje-pu");
            factory.createEntityManager();

        }
        return entityManager;
    }
}
