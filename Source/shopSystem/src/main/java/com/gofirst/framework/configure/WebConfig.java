package com.gofirst.framework.configure;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.gofirst.framework.interceptor.SessionInterceptor;

/**
 * web配置
 * 
 */
@Configuration
@ComponentScan(basePackages = "com.gofirst.framework")
@EnableWebMvc
@EnableCaching
@CacheConfig(cacheManager = "cacheManager")
@EnableScheduling
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * 会话过滤
	 */
	@Inject
	private SessionInterceptor sessionInterceptor = null;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(sessionInterceptor);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// 页面位置的配置以及保证项目安全
		registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");

		registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/views/");
	}

	//
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addRedirectViewController(urlPath, redirectUrl)

	}

	@Bean(name = "cacheManager")
	@Singleton
	public CacheManager getCacheManager() {
		return new ConcurrentMapCacheManager();
	}

	@Bean(name = "multipartResolver")
	@Singleton
	public MultipartResolver getMultipartResolver() {
		return new CommonsMultipartResolver();
	}

}
