package com.gofirst.framework.configure;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gofirst.framework.util.Constants;

/**
 * gson 配置类
 * 
 * 
 */
@Configuration
public class ShiroConfigure {

	
	/**
	 * spring context
	 */
	@Inject
	private ApplicationContext context;
	
	/**
	 * 因为securityManager里面会用到ConfigureProperties
	 * 所以ConfigureProperties必须放在SecurityManager前面
	 */
	@Inject
	private SystemConfigProperties sysConfig = null;

	/**
	 * shiro SecurityManager
	 */
	@Inject
	private SecurityManager securityManager = null;	

	/**
	 * Shiro SecurityManager
	 * 
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Bean(name = "securityManager")
	@Singleton
	public SecurityManager getSecurityManager() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		//根据配置文件拿到对应的realm
		Realm frameworkRealm = (Realm) context.getBean(sysConfig.getProperty(Constants.REALM_BEAN_NAME));
		DefaultSecurityManager sm = new DefaultSecurityManager(frameworkRealm);
		SecurityUtils.setSecurityManager(sm);
		
		DefaultSessionManager ssm = new DefaultSessionManager();
		
		if (!"remote".equals(sysConfig.getProperty(Constants.SESSION_REMOTE))) {
			ssm.setSessionDAO(new EnterpriseCacheSessionDAO());

			sm.setSessionManager(ssm);
			sm.setCacheManager(new MemoryConstrainedCacheManager());
			//设置session过期时间
			long timeout = Long.valueOf(sysConfig.getProperty(Constants.SESSION_TIME_OUT));
			ssm.setGlobalSessionTimeout(timeout);

			return sm;
		} 

		RedisSessionDAO sessionDao = (RedisSessionDAO) Class.forName(sysConfig.getProperty(Constants.REDISSESSIONDAO)).newInstance();
		ssm.setSessionDAO(sessionDao);
		
		RedisManager redisManager = (RedisManager) Class.forName(sysConfig.getProperty(Constants.REDISMANAGER)).newInstance();
		redisManager.setHost(sysConfig.getProperty(Constants.REDIS_HOST));
		redisManager.setPort(Integer.valueOf(sysConfig.getProperty(Constants.REDIS_PORT)));
		redisManager.setExpire(Integer.valueOf(sysConfig.getProperty(Constants.REDIS_EXPIRE)));
		sessionDao.setRedisManager(redisManager);
		
		ssm.setSessionDAO(sessionDao);
		sm.setSessionManager(ssm);
		sm.setCacheManager(new MemoryConstrainedCacheManager());
		
		return sm;
	}


	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {

		AuthorizationAttributeSourceAdvisor aasd = new AuthorizationAttributeSourceAdvisor();
		aasd.setSecurityManager(securityManager);

		return aasd;
	}

}
