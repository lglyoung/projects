package org.im.model;

/**
 * 用户类
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class User {
	private String userId;
	private String uname;
	private String pwd;
	
	public User() {
		super();
	}
	
	public User(String userId, String uname, String pwd) {
		super();
		this.userId = userId;
		this.uname = uname;
		this.pwd = pwd;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * 一次性设置全部属性
	 * @author lglyoung 2016.07.24
	 * @param userId
	 * @param uname
	 * @param pwd
	 */
	public void set(String userId, String uname, String pwd) {
		this.userId = userId;
		this.uname = uname;
		this.pwd = pwd;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", uname=" + uname + ", pwd=" + pwd + "]";
	}
}
