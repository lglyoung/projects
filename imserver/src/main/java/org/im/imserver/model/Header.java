package org.im.imserver.model;

/**
 * 报文头部类
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class Header {
	private int contentBytesLen;
	private String packageType;
	
	public Header() {
		super();
	}

	public Header(int contentLen, String packageType) {
		super();
		this.contentBytesLen = contentLen;
		this.packageType = packageType;
	}

	public int getContentLen() {
		return contentBytesLen;
	}

	public void setContentLen(int contentLen) {
		this.contentBytesLen = contentLen;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

}
