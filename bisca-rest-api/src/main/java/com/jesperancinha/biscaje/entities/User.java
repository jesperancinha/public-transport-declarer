package com.jesperancinha.biscaje.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.hibernate.search.annotations.Indexed;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */
@Entity
@Indexed
@Proxy(lazy = false)
@Builder
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String name;

	private String password;

	@Basic
	private java.sql.Timestamp lastlog;

	private String addres;
}
