package com.jesperancinha.biscaje.service;

import static com.google.common.truth.Truth.assertThat;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jesperancinha.biscaje.BiscaJeLauncher;
import com.jesperancinha.biscaje.model.Player;
import com.jesperancinha.biscaje.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiscaJeLauncher.class)
public class UserRepositoryIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	@Transactional
	void setUp() {
		entityManager.joinTransaction();

	}

	@Test
	@Transactional
	public void createUser() {
		entityManager.joinTransaction();

		final String testName = "I have a great user name";
		final String testPassword = "this is not a password but it will do";
		final String testAddress = "this is my addres";
		final String testTelephone = "111223344";
		final String testEmail = "this is such an awesome mail";
		final Player testPlayer = Player.builder().build();
		final User testUser = User.builder().name(testName).email(testEmail).telephone(testTelephone)
				.password(testPassword).addres(testAddress).player(testPlayer).build();

		final Player savedPlayer = playerRepository
				.save(testPlayer);
		final User savedUser = userRepository.save(testUser);
		savedPlayer.setUser(savedUser);
		final Player afterSavePlayer = playerRepository.save(savedPlayer);

		assertThat(savedPlayer).isNotNull();
		assertThat(savedPlayer.getId()).isNotNull();
		assertThat(afterSavePlayer.getUser()).isEqualTo(savedUser);
	}

}