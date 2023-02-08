package com.blurdel.sdjpajdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.blurdel.sdjpajdbc.dao.AuthorDao;
import com.blurdel.sdjpajdbc.dao.AuthorDaoImpl;
import com.blurdel.sdjpajdbc.domain.Author;

@ActiveProfiles("mysql")
@DataJpaTest
//@ComponentScan(basePackages = {"com.blurdel.sdjpajdbc.dao"})
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoIntegrationTest {

	@Autowired
	AuthorDao authorDao;
	
	@Test
	void test() {
//		fail("Not yet implemented");
		
		Author author = authorDao.getById(1L);
		assertThat(author).isNotNull();
	}

}
