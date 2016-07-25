package org.im.imserver.util;

public class User {
	private String username;
	private String pwd;
	
	public User() {
		super();
	}
	public User(String username, String pwd) {
		super();
		this.username = username;
		this.pwd = pwd;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", pwd=" + pwd + "]";
	}	
}