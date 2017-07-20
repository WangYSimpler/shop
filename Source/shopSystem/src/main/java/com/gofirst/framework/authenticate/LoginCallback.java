package com.gofirst.framework.authenticate;

import org.apache.shiro.subject.Subject;

import com.gofirst.framework.session.FrameworkSession;

/**
 *   login之后的回调方法
 */
public interface LoginCallback {

	public void callback(FrameworkSession session, Subject subject);
}
