package com.gofirst.framework.test;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring 上下文环境
 * @author WangY
 *
 */
@Configuration
@ComponentScan( basePackages = {"com.gofirst"})
@EnableAspectJAutoProxy
public class SpringApplication {
	/**
	 * Spring 上下文
	 */
	private static AnnotationConfigApplicationContext ctx = null; 
	
	private static Lock lock = new ReentrantLock(); 
	
	public static ApplicationContext getApplicationContext(){
		if(ctx!=null){
			return ctx;	
		}
		
		try{
			((ReentrantLock) lock).lock();
			if(ctx==null){
				ctx = new AnnotationConfigApplicationContext();
				ctx.scan("com.gofirst");
				ctx.refresh();
			}
		}finally{
			((ReentrantLock) lock).unlock();
		}
		return ctx;	
		
	}
	
	public static Lock getLock(){
		return lock;
		
		
	}
}

