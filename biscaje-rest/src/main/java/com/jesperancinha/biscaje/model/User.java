package com.jesperancinha.biscaje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Proxy(lazy = false)
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
public class User {

    private String id;

    private Player player;

    private String name;

    private String password;

    private Timestamp lastlog;

    private String addres;

    private String email;

    private String telephone;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Player.class, cascade = CascadeType.ALL, optional = false)
    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Basic
    public Timestamp getLastlog() {
        return lastlog;
    }

    public String getAddres() {
        return addres;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }
}
