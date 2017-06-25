package com.gofirst.framework.customRepository.impl;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gofirst.framework.customRepository.FrameworkRepository;

/**
 * 
 * 给所有repository都加上自定义方法
 *
 * @param <T>
 * @param <ID>
 */
public class FrameworkRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, Serializable> implements FrameworkRepository<T, Serializable> {

	private final EntityManager entityManager;
	
	public FrameworkRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		
		//keep the EntityManager around to used from the newly introduced methods.
		this.entityManager = em;
	}

	/**
	 * 部分跟新
	 */
	@Override
	@Transactional(readOnly = false)
	public void partUpdate(Serializable id, Object updateObj) throws IllegalArgumentException, IllegalAccessException {
		//从数据库根据id取值
		Object queriedObj = entityManager.find(updateObj.getClass(), id);
		//比较需要跟新的对象，看哪些值更改了，跟新到从数据库取出的对象
		Field[] updateFields = updateObj.getClass().getDeclaredFields();
		Field[] oldFields = queriedObj.getClass().getDeclaredFields();
		int index = 0;
		for (Field field : updateFields) {
			field.setAccessible(true);
			Object updateValue = field.get(updateObj);
			//需要跟新的属性不为空，则跟新
			if(updateValue != null) {
				oldFields[index].setAccessible(true);
				oldFields[index].set(queriedObj, updateValue);
			}
			index ++;
		}
		//跟新到数据库
		entityManager.merge(queriedObj);
	}

}
