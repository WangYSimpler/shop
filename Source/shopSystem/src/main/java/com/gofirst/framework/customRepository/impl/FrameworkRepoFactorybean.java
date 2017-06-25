package com.gofirst.framework.customRepository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 *
 * 自定义repository factory,给所有repository都加上自定义方法
 *
 * @param <R>
 * @param <T>
 * @param <I>
 */
public class FrameworkRepoFactorybean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {
	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new MyRepositoryFactory(em);
	}

	private static class MyRepositoryFactory<T, I extends Serializable> extends
			JpaRepositoryFactory {
		private final EntityManager em;

		public MyRepositoryFactory(EntityManager em) {
			super(em);
			this.em = em;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new FrameworkRepositoryImpl<T, I>(
					(Class<T>) metadata.getDomainType(), em);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return FrameworkRepositoryImpl.class;
		}
	}
}
