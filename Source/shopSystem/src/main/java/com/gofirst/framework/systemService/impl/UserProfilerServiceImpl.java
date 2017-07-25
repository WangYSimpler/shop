package com.gofirst.framework.systemService.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.systemService.UserProfilerService;
import com.gofirst.framework.util.Constants;
//import com.google.gson.Gson;

/**
 * 
 * 拿到用户权限和角色服务
 *
 */
@Named
public class UserProfilerServiceImpl implements UserProfilerService{

	/**
	 * spring context
	 */
	@Inject
	private ApplicationContext context;
	
	@Inject
	private SystemConfigProperties sysConfig = null;
	
	/**
	 * gson
	 */
	//@Inject
	//private Gson gson = null;
	/**
	 * shiro realm
	 */
	private AuthorizingRealm frameworkRealm = null;

	@PostConstruct
	public void init() {
		//根据配置文件拿到对应的realm
		frameworkRealm = (AuthorizingRealm) context.getBean(sysConfig.getProperty(Constants.REALM_BEAN_NAME));
	}
	/**
	 * 得到用户的某一种类型的权限
	 * @param sessionId	用户sessionId
	 * @param type	用户类型
	 * @return	权限
	 */
	public String getProfilers(String sessionId, String type) {
		Subject subject = new Subject.Builder().sessionId(sessionId).buildSubject();
		List<String> rolesAndPermission = new ArrayList<String>();
		//检验用户是否已经登陆
		if (subject.isAuthenticated()) {
			subject.hasRole("");
			Cache<Object, AuthorizationInfo> cache = frameworkRealm.getAuthorizationCache();
			Set<Object> keys = cache.keys();
			for (Object key : keys) {
				//所有角色
				rolesAndPermission.addAll(cache.get(key).getRoles());
				Set<String> permissions = (Set<String>)cache.get(key).getStringPermissions();
				//拿到指定类型的权限
				for (String permission : permissions) {
					if (permission.contains(type)) {
						rolesAndPermission.add(permission);
					}
				}
			}
			//return gson.toJson(rolesAndPermission);
			return JSON.toJSONString(rolesAndPermission);
		} else {
			return null;
		}
	}
	
}
