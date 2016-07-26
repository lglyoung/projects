package org.im.imserver.model;

/**
 * 文本消息类
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class Body {
	private User user;
	private String content;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Body [user=" + user + ", content=" + content + "]";
	}
}
