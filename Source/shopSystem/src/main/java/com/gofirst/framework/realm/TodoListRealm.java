package com.gofirst.framework.realm;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.gofirst.framework.service.TUserService;


@Named
public class TodoListRealm extends AuthorizingRealm {
	
	
	@Inject
	private TUserService tUserService;
	/*
    @Inject
	private TestService testService;
	*/
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

		String userNo = (String) getAvailablePrincipal(arg0);
		
		System.out.println(userNo);
		
		//System system = systemRepository.findBySysNameIgnoreCase(sysConfig.getProperty(Constants.SYSTEMNAME));
	
		Set<String> permissions = new TreeSet<String>();
		/*
	    permissions.add("dao:UserRepository.*");
		permissions.add("dao:TInstrumentMarginRepository.*");
		
		permissions.add("service:TestSettleService.*");
		permissions.add("service:TInstrumentMarginService.*");*/
		
		permissions.add("dao:HyUsersRepository.*");
		permissions.add("dao:TUserRepository.*");
		
		permissions.add("dao:*.*");	
		permissions.add("service:*.*");
		//permission = testService.
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(new TreeSet<String>());
		
		
		info.setStringPermissions(permissions);
		///end

		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		boolean AuthUserResult =  tUserService.isAuthenticatedUser(token.getUsername(), String.valueOf(token.getPassword()));
		if (AuthUserResult) {
			return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), "testRealm");
		} else {
			throw new AccountException("wrong password");
		}

	}

}