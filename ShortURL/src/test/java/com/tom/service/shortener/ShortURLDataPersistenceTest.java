package com.tom.service.shortener;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.tom.service.shortener.common.URLEncoding;
import com.tom.service.shortener.model.URL;
import com.tom.service.shortener.repository.URLRepository;

import net.datafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings("deprecation")
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
class ShortURLDataPersistenceTest {

	@Autowired
	private URLRepository repository;

	@Autowired
	private URLEncoding encoding;
	
	private URL url;

	private final int ITEM_QUANTITY = 100;

	private final Faker faker = new Faker();

	@Test
	@Transactional
	@Order(1)
	@DisplayName("1 - Data Insertion Test")
	void dataInsertionTest() throws Exception {
		for (int i = 0; i < ITEM_QUANTITY; i++) {
			String originalURL = faker.internet().url();
			LocalDateTime data = faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			url = new URL();
			url.setShortUrl(encoding.generateURL());
			url.setOriginalUrl(originalURL);
			url.setAccessCount(faker.number().numberBetween(0, 1000));
			url.setDateCreated(data);
			url.setExpirationTime(data.plus(Duration.ofDays(30)));
			url.setLastAccessTime(data.plus(Duration.ofDays(faker.number().numberBetween(1, 6))));
			repository.save(url);
		}

		assertThat(repository.count()).isEqualTo(ITEM_QUANTITY);
	}

	@Test
	@Order(2)
	@Transactional
	@DisplayName("2 - Find Data Test")
	void findDataTest() throws Exception {
		for (int i = 0; i < ITEM_QUANTITY; i++) {
			String originalURL = faker.internet().url();
			LocalDateTime data = faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			url = new URL();
			url.setShortUrl(encoding.generateURL());
			url.setOriginalUrl(originalURL);
			url.setAccessCount(faker.number().numberBetween(0, 1000));
			url.setDateCreated(data);
			url.setExpirationTime(data.plus(Duration.ofDays(30)));
			url.setLastAccessTime(data.plus(Duration.ofDays(faker.number().numberBetween(1, 6))));
			repository.save(url);
		}
		List<URL> urls = repository.findAll();
		assertThat(urls).hasSize(ITEM_QUANTITY);
	}

	@Test
	@Order(3)
	@Transactional
	@DisplayName("3 - Delete Data Test")
	void dataDeleteTest() throws Exception {
		repository.findAll().forEach(repository::delete);
		assertThat(repository.count()).isZero();
	}

}
