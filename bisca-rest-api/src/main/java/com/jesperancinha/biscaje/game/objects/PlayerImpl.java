package com.jesperancinha.biscaje.game.objects;

import java.util.List;

import com.jesperancinha.biscaje.model.Card;
import com.jesperancinha.biscaje.model.Player;
import lombok.Getter;

@Getter
public class PlayerImpl {
	private Integer orderId;

	private String playerName;

	private Player nextPlayer;

	PlayerImpl(String playerName) {
		this.playerName = playerName;
	}

	public Long getPlayerPoints() {
		return null;
	}

	public List<Card> getPlayerCards() {
		return null;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}
}
