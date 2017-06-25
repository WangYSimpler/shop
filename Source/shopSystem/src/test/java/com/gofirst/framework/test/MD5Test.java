package com.gofirst.framework.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gofirst.framework.util.MD5Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringApplication.class})
@WebAppConfiguration
public class MD5Test {
	/*@Inject
	private MD5Util md5Test;*/

	 @Test
	 public void md5Test(){
		 System.out.println(MD5Util.md5("2"));
	 }

}
