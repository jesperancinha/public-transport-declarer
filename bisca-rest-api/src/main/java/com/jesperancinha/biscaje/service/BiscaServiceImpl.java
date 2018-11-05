package com.jesperancinha.biscaje.service;

import com.jesperancinha.biscaje.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@ApplicationScoped
@Named("biscaService")
public class BiscaServiceImpl implements BiscaService {

    private EntityManager entityManager;

    public BiscaServiceImpl() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("biscaje-pu");
        entityManager = factory.createEntityManager();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }

    @Override
    public boolean createUser(String userName, String userPassword, Date date) {
        createUser(new User(userName, userPassword, date));
        return false;
    }

    private boolean createUser(User user) {
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return false;
    }
}
