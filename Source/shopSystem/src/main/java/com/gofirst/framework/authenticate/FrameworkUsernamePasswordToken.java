package com.gofirst.framework.authenticate;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * 框架层封装UsernamePasswordToken，用以支持type
 *
 */
public class FrameworkUsernamePasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String type;

	public FrameworkUsernamePasswordToken() {
		super();
	}

	public FrameworkUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public FrameworkUsernamePasswordToken(String username, char[] password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public FrameworkUsernamePasswordToken(String username, char[] password, String host) {
		super(username, password, host);
	}

	public FrameworkUsernamePasswordToken(String username, char[] password) {
		super(username, password);
	}

	public FrameworkUsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public FrameworkUsernamePasswordToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public FrameworkUsernamePasswordToken(String username, String password, String host) {
		super(username, password, host);
	}

	public FrameworkUsernamePasswordToken(String username, String password) {
		super(username, password);
	}

	public FrameworkUsernamePasswordToken(String username, String password, String host, String type) {
		super(username, password, host);
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
