package com.jesperancinha.biscaje.service;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jesperancinha.biscaje.BiscaJeLauncher;
import com.jesperancinha.biscaje.model.Player;
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

	private static final String STAY_NAME = "stay name";
	private static final String STAY_MAIL = "stay mail";
	private static final String STAY_TELEPHONE = "stay telephone";

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	@Transactional
	public void setUp() {
		entityManager.joinTransaction();
		Player testPlayer = Player.builder().name(STAY_NAME).email(STAY_MAIL).telephone(STAY_TELEPHONE).build();
		playerRepository
				.save(testPlayer);
		entityManager.persist(testPlayer);
		entityManager.flush();
	}

	@Test
	@Transactional
	public void testCreatePlayer() {
		entityManager.joinTransaction();
		String testName = "I'm a great name";
		String testEmail = "this is such an awesome mail";
		String testTelephone = "111223344";

		final Player savedPlayer = playerRepository
				.save(Player.builder().name(testName).email(testEmail).telephone(testTelephone).build());

		assertThat(savedPlayer).isNotNull();
		assertThat(savedPlayer.getId()).isNotNull();
		assertThat(savedPlayer.getName()).isEqualTo(testName);
		assertThat(savedPlayer.getEmail()).isEqualTo(testEmail);
		assertThat(savedPlayer.getTelephone()).isEqualTo(testTelephone);
		assertThat(savedPlayer.getUser()).isNull();

		entityManager.persist(savedPlayer);
		entityManager.flush();
	}

	@Test
	@Transactional
	public void testReadPlayer() {
		Player playerByName = playerRepository.findByName(STAY_NAME);

		assertThat(playerByName).isNotNull();
		assertThat(playerByName.getId()).isNotNull();
		assertThat(playerByName.getName()).isEqualTo(STAY_NAME);
		assertThat(playerByName.getEmail()).isEqualTo(STAY_MAIL);
		assertThat(playerByName.getTelephone()).isEqualTo(STAY_TELEPHONE);
		assertThat(playerByName.getUser()).isNull();
	}

	@Test
	@Transactional
	public void testUpdatePlayer() {
		entityManager.joinTransaction();
		Player playerByName = playerRepository.findByName(STAY_NAME);
		String testName = "My new adorable name!";
		playerByName.setName(testName);

		final Player newPlayerUpdated = playerRepository.save(playerByName);

		assertThat(newPlayerUpdated).isNotNull();
		assertThat(newPlayerUpdated.getId()).isNotNull();
		assertThat(newPlayerUpdated.getName()).isEqualTo(testName);
		assertThat(newPlayerUpdated.getEmail()).isEqualTo(STAY_MAIL);
		assertThat(newPlayerUpdated.getTelephone()).isEqualTo(STAY_TELEPHONE);
		assertThat(newPlayerUpdated.getUser()).isNull();

		entityManager.persist(newPlayerUpdated);
		entityManager.flush();

		final Player oldNameCheck = playerRepository.findByName(STAY_NAME);
		assertThat(oldNameCheck).isNull();

	}

	@Test
	@Transactional
	public void testDeletePlayer() {
		entityManager.joinTransaction();
		final Player playerByName = playerRepository.findByName(STAY_NAME);
		assertThat(playerByName).isNotNull();

		playerRepository.delete(playerByName);
		final Player playerByNameAfterDeletion = playerRepository.findByName(STAY_NAME);

		assertThat(playerByNameAfterDeletion).isNull();
	}

}