package com.gofirst.framework.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.gofirst.framework.service.TUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringApplication.class})
@WebAppConfiguration
public class UserServiceTest {
	
	@Inject
	private TUserService usersService;
	
	@Test
	public void testAuthenticate() {
		boolean actual = usersService.isAuthenticatedUser("1", "1");
		
		
		Assert.assertEquals(true, actual);
	}

}
