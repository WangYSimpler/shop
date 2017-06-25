package com.gofirst.framework.dao;

import javax.inject.Named;

//import com.gofirst.framework.annotation.SQL;
//import com.gofirst.framework.annotation.SQL.DBEnum;

@Named
public class CommonQuery {
	
//	@SQL(database = DBEnum.MYSQL, isDynamic = false, sql = "select first_name, last_name from t_user where first_name = ? and last_name = ?")
//	public void searchUser (String firstName, String lastName){};
//	
//	@SQL(database=DBEnum.MYSQL, isDynamic = true, sql = "select first_name, last_name from t_user where 1 = 1 {and first_name = ? } {and last_name = ?}")
//	public void searchUserDynamicSingle(String firstName, String lastName){};
//
//	@SQL(database = DBEnum.MYSQL, isDynamic = false, sql = "select first_name, last_name from t_user where {first_name in (?)}")
//	public void searchUserIn (String[] parms){};
//	
//	@SQL(database = DBEnum.MYSQL, isDynamic = true, sql = "select first_name, last_name from t_user where 1 = 1 {and first_name = ?} {and first_name in (?)} {and last_name = ?}")
//	public void searchUserInDynamic (String[] parms){};
	
}
