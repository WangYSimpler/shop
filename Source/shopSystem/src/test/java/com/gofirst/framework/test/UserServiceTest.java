package com.gofirst.framework.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//import com.gofirst.framework.bean.TOrder;
//import com.gofirst.framework.dao.TOrderRepository;
import com.gofirst.framework.service.TUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringApplication.class})
@WebAppConfiguration
public class UserServiceTest {
	
	@Inject
	private TUserService usersService;
	
	@Test
	public void testAuthenticate() {
		boolean actual = usersService.isAuthenticatedUser("1", "888888");
		
		
		Assert.assertEquals(true, actual);
	}
	
	/*@Inject
	private TOrderRepository tOrderRepository;
	
	@Test
	public void testAuthenticate() {
		List<TOrder> lorder= tOrderRepository.findByOrderNoContaining("201706270001");
		
		System.out.println(lorder.size());
		
	}*/
	

}
