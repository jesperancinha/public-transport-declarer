package com.jesperancinha.biscaje.service;

import static com.google.common.truth.Truth.assertThat;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jesperancinha.biscaje.BiscaJeLauncher;
import com.jesperancinha.biscaje.model.Player;
import com.jesperancinha.biscaje.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * This integration tests runs through the CRUD operaions for the Player data type Objects
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
		BiscaJeLauncher.class,
})
@Profile("test")
public class PlayerRepositoryIntegrationTest {

	private static final User USER_BUILD = User.builder().build();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private EntityManager entityManager;

	private Player testPlayer;

	@BeforeEach
	@Transactional
	public void setUp() {
		entityManager.joinTransaction();
		User testUser = userRepository.save(USER_BUILD);
		testPlayer = Player.builder().user(
				USER_BUILD).build();
		playerRepository
				.save(testPlayer);
	}

	@Test
	@Transactional
	public void testCreatePlayer() {
		entityManager.joinTransaction();

		userRepository.save(USER_BUILD);
		final Player savedPlayer = playerRepository
				.save(Player.builder().user(USER_BUILD).build());

		assertThat(savedPlayer).isNotNull();
		assertThat(savedPlayer.getId()).isNotNull();
		assertThat(savedPlayer.getUser()).isNotNull();

	}

	@Test
	@Transactional
	public void testReadPlayer() {
		Player playerByName = playerRepository.findById(testPlayer.getId()).orElse(null);

		assertThat(playerByName).isNotNull();
		assertThat(playerByName.getId()).isNotNull();
		assertThat(playerByName.getUser()).isNotNull();
		assertThat(playerByName.getPoints()).isNull();
	}

	@Test
	@Transactional
	public void testUpdatePlayer() {
		entityManager.joinTransaction();
		Player playerByName = playerRepository.findById(testPlayer.getId()).orElse(null);
		long testPoints = 10;
		playerByName.setPoints(testPoints);

		final Player newPlayerUpdated = playerRepository.save(playerByName);

		assertThat(newPlayerUpdated).isNotNull();
		assertThat(newPlayerUpdated.getId()).isNotNull();
		assertThat(newPlayerUpdated.getUser()).isNotNull();

		final Player currentUpdatedPlayer = playerRepository.findById(testPlayer.getId()).orElse(null);
		assertThat(currentUpdatedPlayer).isNotNull();
		assertThat(currentUpdatedPlayer.getPoints()).isEqualTo(testPoints);

	}

	@Test
	@Transactional
	public void testDeletePlayer() {
		entityManager.joinTransaction();
		final Player playerByName = playerRepository.findById(testPlayer.getId()).orElse(null);
		assertThat(playerByName).isNotNull();

		playerRepository.delete(playerByName);
		final Player playerByNameAfterDeletion = playerRepository.findById(testPlayer.getId()).orElse(null);

		assertThat(playerByNameAfterDeletion).isNull();
	}

}