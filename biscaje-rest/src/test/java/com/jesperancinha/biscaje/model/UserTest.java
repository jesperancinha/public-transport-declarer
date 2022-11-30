package com.jesperancinha.biscaje.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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