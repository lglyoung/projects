package org.im.imserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.im.imserver.model.Body;
import org.im.imserver.model.Header;
import org.im.imserver.util.ReceiverUtil;

/**
 * 读处理器
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class ReadHandler implements IEventHandler {
	private SocketChannel sc;
	private SelectionKey key;
	private boolean is4BytesReceived = false;
	
	private ByteBuffer first4BytesBuff = ByteBuffer.allocate(4);	//保存报文段前4个字节
	private ByteBuffer headerBuff;				//保存头部
	private Header header;
	private Body body;
		
	/**
	 * 构造器
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public ReadHandler(SelectionKey key) {
		this.key = key;
		sc = (SocketChannel) key.channel();
	}

	@Override
	public void handle() throws Exception {
		if(header == null) {
			//读取头部
			header = readHeader();
		} else {	//成功读到header
			body = readBody();
			if(body != null) {
				//1 把header和body传递出去
				System.out.println(header.toString()+body.toString());				
				
				//2 初始化
				init();
			}
		}
	}
	
	/**
	 * 读取头部
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public Header readHeader() {
		if(!is4BytesReceived) {
			int r = ReceiverUtil.readFromSc(sc, first4BytesBuff);
			if(r == 1) {
				is4BytesReceived = true; 	//成功接收报文的前4个字节					
			} else if(r == -1) {			//对方的socket channel关闭
				closeSc();
			}	
		} else {
			if(headerBuff == null) {
				first4BytesBuff.flip();		//为读取做好准备
				headerBuff = ByteBuffer.allocate(first4BytesBuff.getInt());
			}
			if(header == null) {
				int r = ReceiverUtil.readFromSc(sc, headerBuff);
				if(r == 1) {
					headerBuff.flip();			//为读取做好准备
					header = ReceiverUtil.readObjFromBuff(headerBuff, Header.class);
					
					//返回header对象
					return header;
				} else if(r == -1) {			//对方的socket channel关闭
					closeSc();
				}
			}	
		}
		return null;
	}
	
	/**
	 * 读取body
	 * @author lglyoung 2016.07.25
	 * @version 1.0.0
	 */
	public Body readBody() {
		ByteBuffer contentBuff = ByteBuffer.allocate(header.getContentLen());
		int r = ReceiverUtil.readFromSc(sc, contentBuff);
		if(r == 1) {
			contentBuff.flip();
			body = ReceiverUtil.readObjFromBuff(contentBuff, Body.class);
			return body;
		} else if(r == -1) {
			closeSc();
		}
		return null;
	}
	
	/**
	 * 根据读取的字节数判断是否关闭当前socket channel
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public void closeSc() {
		System.out.println("server closed a socket");
		try {
			sc.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		key.cancel();
		int curConnNum = ImServer.getInstance().getConnNum()-1;
		ImServer.getInstance().setConnNum(curConnNum);
	}
	
	/**
	 * 接收完之后初始化
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	private void init() {
		is4BytesReceived = false;
		first4BytesBuff.clear();
		headerBuff = null;
		header = null;
		body = null;
	}
}
