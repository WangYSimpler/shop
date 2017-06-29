package com.gofirst.framework.customRepository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * 给所有repository都加上自定义方法
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface FrameworkRepository <T, ID extends Serializable> extends CrudRepository<T, ID>{

	/**
	 * 部分更新
	 * @param id
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void partUpdate(ID id, Object obj) throws IllegalArgumentException, IllegalAccessException;
	
}
