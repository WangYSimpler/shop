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
public class HyUserServiceTest {
	
	@Inject
	private TUserService hyUsersService;
	
	@Test
	public void testAuthenticate() {
		boolean actual = hyUsersService.isAuthenticatedUser("1", "888888");
		Assert.assertEquals(true, actual);
	}
	
	/*@Inject
	private HyUsersRepository hyUsersRepository;
	
	//@Inject
	private HyUsers hyUsers = new HyUsers();
	
	@Test
	public void testHyUsersSave() {
		hyUsers.setUserId(new BigDecimal("22"));
		hyUsers.setUserNo("12");
		hyUsers.setUserName("wangyong");
		hyUsers.setDeleteFlag('0');
		hyUsers.setCreateDatetime(new Date());
		hyUsers.setUserType('1');
		hyUsers.setCreateUser("wang");
		hyUsers.setOrganizationId(new BigDecimal("1"));
		hyUsers.setUserPd("12");
		hyUsersRepository.save(hyUsers);
		//boolean actual = hyUsersService.isAuthenticatedUser("1", "1");
		//Assert.assertEquals(true, actual);
	}*/

}
