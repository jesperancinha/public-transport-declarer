package com.jesperancinha.biscaje.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Setter
@Table(name = "Player")
public class Player {


	private String id;

	private User user;

	private String name;

	private String email;

	private String telephone;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	@PrimaryKeyJoinColumn
	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephone() {
		return telephone;
	}
}
