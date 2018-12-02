package com.jesperancinha.biscaje.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy = false)
@Builder
@Setter
@Table(name = "User")
public class User {

	private String id;

	private Player player;

	private String name;

	private String password;

	private java.sql.Timestamp lastlog;

	private String addres;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Player.class, cascade = CascadeType.ALL)
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
}
