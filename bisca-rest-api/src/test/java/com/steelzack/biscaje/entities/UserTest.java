package com.steelzack.biscaje.entities;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */
public class UserTest {

    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setUpEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("bisca");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }
}