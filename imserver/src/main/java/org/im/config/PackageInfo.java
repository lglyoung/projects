package org.im.config;

/**
 * 报文类型
 * @author lglyoung 2016.07.24
 */
public class PackageInfo {
	//报文前几个字节的长度
	public final static int FIRST_BYTES_LEN = 4;
	
	//报文类型
	public final static String cmdBasePackage = "org.im.cmd.";
	public final static String SEND_MSG_CMD = "SendMsgCmd";		//发送消息命令
}
