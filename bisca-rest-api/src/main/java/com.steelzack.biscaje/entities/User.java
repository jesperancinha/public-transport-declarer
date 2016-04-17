package com.steelzack.biscaje.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private long id;

    private String name;

    private String password;

    private Date lastlog;

    public User(String name, String password, Date lastlog) {
        this.name = name;
        this.password = password;
        this.lastlog = lastlog;
    }
}
