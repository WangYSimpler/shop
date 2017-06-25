package com.gofirst.framework.session.impl;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.gofirst.framework.session.FrameworkSession;
import com.gofirst.framework.util.Constants;

/**
*
* 自定义框架曾的session
*
*/
public class FrameworkSessionImpl implements FrameworkSession {
	
	/**
	 * Shiro session
	 */
	private Session session;
	
	/**
	 * 用户名
	 */
	private String userNo;
	
	public FrameworkSessionImpl() {
		super();
	}

	public FrameworkSessionImpl(Session session) {
		super();
		this.session = session;
	}

	public FrameworkSessionImpl(Session session, String userNo) {
		super();
		this.session = session;
		this.userNo = userNo;
	}

	@Override
	public Object getAttribute(Object key) {
		return session.getAttribute(key);
	}

	@Override
	public void setAttribute(Object key, Object value) {
		if (key instanceof String) {
			protectKeys((String)key);
		}
		session.setAttribute(key, value);
	}

	@Override
	public String getUserId() {
		return userNo;
	}
	
	/**
	 * protect used keys by framework
	 */
	private void protectKeys(String key) {
		String protectedkeys = Constants.SESSION_PERMISSION;
		if (protectedkeys.contains(key)) {
			throw new RuntimeException("the key is used by framework");
		}
	}
	
	/**
	 * 根据threadLocal拿到当前的session
	 * @return
	 */
	public static FrameworkSessionImpl getSession() {
		//拿到session
		String sessionId = FrameworkSession.getSessionId();
		Subject subject = new Subject.Builder().sessionId(sessionId)
				.buildSubject();
		String userNo = subject.getPrincipal().toString();
		return new FrameworkSessionImpl(subject.getSession(), userNo);
	}
	
	/**
	 * 得到框架session
	 * @param session	shiro session
	 * @param userNo	用户名
	 * @return
	 */
	public static FrameworkSessionImpl createSession(Session session, String userNo) {
		return new FrameworkSessionImpl(session, userNo);
	}

}
