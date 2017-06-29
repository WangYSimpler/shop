package com.gofirst.framework.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gofirst.framework.annotation.EntityClass;
import com.gofirst.framework.annotation.Forbid;
import com.gofirst.framework.annotation.Gateway;
import com.gofirst.framework.bean.TOrder;
import com.gofirst.framework.customRepository.FrameworkRepository;
@Repository(value="TOrderRepository")
@EntityClass(entityClass=TOrder.class)
@Forbid(forbiddenMethods = {""})
@Gateway
public interface TOrderRepository extends CrudRepository<TOrder, Long>,FrameworkRepository<TOrder, Long>{
	
	///查询全部案件
	public Iterable<TOrder>findAll(Pageable pageable);
	
	public Page<TOrder>findByFlag(Character flag, Pageable pageable);
	public Page<TOrder>findByOrderNo(String orderNo, Pageable pageable);
	
	//有效订单: '0' 为有效
	public List<TOrder>findByOrderNoContaining(String orderNo);
}
