package com.gofirst.framework.configure;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gofirst.framework.customRepository.impl.FrameworkRepoFactorybean;
import com.gofirst.framework.util.Constants;
import com.mchange.v2.c3p0.DataSources;

/**
 * Jpa 配置
 * @author WangY
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gofirst", repositoryFactoryBeanClass = FrameworkRepoFactorybean.class)
@Configuration
public class JpaConfig {

	@Inject
	private SystemConfigProperties sysConfig = null;
	
	/**
	 * dataSource
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Bean
	public DataSource dataSource() throws SQLException {
		DataSource dataSource = DataSources.unpooledDataSource();
		return dataSource;
	}

	/**
	 * entityManagerFactory
	 * @return
	 * @throws SQLException
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
			throws SQLException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		//vendorAdapter.setDatabase(Database.MYSQL);
		
		///获取配置文件中数据类型 20170427 WangY
		String hibernateDBSubject = sysConfig.getProperty(Constants.HIBERNATE_DB_SUBJECT);
		vendorAdapter.setDatabase(Database.valueOf(hibernateDBSubject));
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		//从配置文件读取hibernate属性
		String hibernateAuto = sysConfig.getProperty(Constants.HIBERNATE_STRATEGY);
		if (hibernateAuto != null && !hibernateAuto.equals(Constants.EMPTY_STRING)) {
			vendorAdapter.getJpaPropertyMap().put(Constants.HIBERNATE_STRATEGY, hibernateAuto);
		}

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(sysConfig.getProperty(Constants.BASE_PACKAGE, "com.gofirst"));
		factory.setDataSource(dataSource());

		return factory;
	}

	/**
	 * transactionManager
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}
	
}
