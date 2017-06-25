package com.gofirst.framework.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gofirst.framework.annotation.EntityClass;
import com.gofirst.framework.annotation.Forbid;
import com.gofirst.framework.annotation.Gateway;
import com.gofirst.framework.bean.TUser;
import com.gofirst.framework.customRepository.FrameworkRepository;

@Repository(value="UserRepository")
@EntityClass(entityClass=TUser.class)
@Forbid(forbiddenMethods={"deleteAll"}) 
@Gateway
public interface TUserRepository  extends CrudRepository<TUser, Long>,FrameworkRepository<TUser, Long>{

	public TUser findByUserNoAndFlag(String userNo,char flag);
	//public User findByUserNo(String userNo);
	
	public Iterable<TUser> findAll(Pageable pageable);
	public List<TUser> findByUserNoAndFlagContaining(String userNo,char flag);
	public Page<TUser> findByUserNoAndFlagContaining(String userNo,char flag, Pageable pageable);
}
