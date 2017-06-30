package com.gofirst.framework.page;

import java.sql.ResultSet;

public interface FrameworkPageable extends ResultSet {

	/**
	 * 返回分页大小
	 */
	int getPageSize();
	
	/**
	 * 返回当前页号
	 */
	int getCurrentPage();

	/**
	 * 返回总页数
	 */
	int getTotalPageCount();

	/**
	 * 返回当前页的记录条数
	 */
	int getCurrentPageRowsCount();

	/**
	 * 转到指定页
	 */
	void gotoPage(int page);

	/**
	 * 设置分页大小
	 */
	void setPageSize(int pageSize);

	/**
	 * 返回总记录行数
	 */
	int getRowsCount();

	/**
	 * 转到当前页的第一条记录
	 * @exception java.sql.SQLException
	 * 异常说明。
	 */
	void pageFirst() throws java.sql.SQLException;

	/**
	 * 转到当前页的最后一条记录
	 * @exception java.sql.SQLException 异常说明。
	 */
	void pageLast() throws java.sql.SQLException;

	
	
}