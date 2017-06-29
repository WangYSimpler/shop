package com.gofirst.framework.bean;

public class PagingResponseResult extends ResponseResult{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1032695410647623136L;
	/**
	 * 数据所有的条数
	 */
	private long totalCount;
	/**
	 * 所有的页数
	 */
	private int pageCount;
	
	public PagingResponseResult() {
		super();
	}

	public PagingResponseResult(long totalCount, int pageCount) {
		super();
		this.totalCount = totalCount;
		this.pageCount = pageCount;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	

	

}
