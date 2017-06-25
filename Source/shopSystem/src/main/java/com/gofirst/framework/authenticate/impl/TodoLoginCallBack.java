package com.gofirst.framework.authenticate.impl;

import javax.inject.Named;

import org.apache.shiro.subject.Subject;

import com.gofirst.framework.authenticate.LoginCallback;
import com.gofirst.framework.session.FrameworkSession;

@Named
public class TodoLoginCallBack implements LoginCallback {
	

	@Override
	public void callback(FrameworkSession session, Subject subject) {
		session.setAttribute("userNo", subject.getPrincipal().toString());
	}

}
