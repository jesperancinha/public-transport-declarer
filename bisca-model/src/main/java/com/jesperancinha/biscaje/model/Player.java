package com.jesperancinha.biscaje.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.util.List;

import lombok.Builder;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Setter
@Table(name = "Player")
public class Player {

	private String id;

	@NotNull
	private User user;

	private Long points;

	private List<Card> cards;

	private Integer orderId;

	private Player nextPlayer;

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

	public Long getPoints() {
		return points;
	}

	@OneToMany(targetEntity = Card.class, mappedBy = "id", fetch = FetchType.EAGER)
	public List<Card> getCards() {
		return cards;
	}

	public Integer getOrderId() {
		return orderId;
	}

	@OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "next_player_id", nullable = false)
	@PrimaryKeyJoinColumn
	public Player getNextPlayer() {
		return nextPlayer;
	}
}