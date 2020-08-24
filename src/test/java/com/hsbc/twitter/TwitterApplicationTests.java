package com.hsbc.twitter;

import com.hsbc.twitter.domain.tweet.boundary.TweetsRepository;
import com.hsbc.twitter.domain.user.boundary.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class TwitterApplicationTests {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private TweetsRepository tweetsRepository;

	@Test
	void contextLoads() {
		assertNotNull(usersRepository);
		assertNotNull(tweetsRepository);
	}

}
