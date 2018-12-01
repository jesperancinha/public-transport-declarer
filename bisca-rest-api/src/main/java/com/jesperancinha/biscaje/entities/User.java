package com.jesperancinha.biscaje.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */
@Entity
@Indexed
@Proxy(lazy = false)
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String password;

    private Date lastlog;

    public User(String name, String password, Date lastlog) {
        this.name = name;
        this.password = password;
        this.lastlog = lastlog;
    }
}
