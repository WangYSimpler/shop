package com.gofirst.framework.systemService;


/**
 * 
 * 拿到用户权限和角色服务
 *
 */
public interface UserProfilerService {

	/**
	 * 得到用户的某一种类型的权限
	 * @param sessionId	用户sessionId
	 * @param type	用户类型
	 * @return	权限
	 */
	public String getProfilers(String sessionId, String type);
}
