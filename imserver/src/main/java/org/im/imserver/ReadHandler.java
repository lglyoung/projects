package org.im.imserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.im.imserver.config.PackageType;
import org.im.imserver.model.Header;
import org.im.imserver.model.Text;
import org.im.imserver.util.CharsetUtil;
import org.im.imserver.util.JsonUtil;

/**
 * 读处理器
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class ReadHandler implements IEventHandler {
	private SocketChannel sc;
	private SelectionKey key;
	private boolean isStartReceived = false;	//如果为true，表明还没接收完上次的发送的数据
	private ByteBuffer first4BytesBuff;			//保存报文段前4个字节
	private ByteBuffer headerBuff;				//保存头部	
	private ByteBuffer contentBuff; 			//保存内容
	private Header header;
	private int HeaderBytesLen = -1;			//头部的字节数
	private int contentBytesLen = -1;			//内容的字节数
		
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
		if(!isStartReceived) {
			isStartReceived = true;
			readPackageFirst4Int();
		} else {
			if(HeaderBytesLen == -1) {
				readPackageFirst4Int();
			} else {
				if(header == null) {
					readHeader();					
				} else {
					readContent();
				}
			}
		}
	}
	
	/**
	 * 读报文的前4个字节
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @throws IOException 
	 */
	public void readPackageFirst4Int() throws IOException {
		if(first4BytesBuff == null) {
			first4BytesBuff = ByteBuffer.allocate(4);
		}
		
		int readNum = 0;
		for(readNum = sc.read(first4BytesBuff); readNum > 0; readNum = sc.read(first4BytesBuff)) {
			if(first4BytesBuff.position() == 4) {
				first4BytesBuff.flip();
				HeaderBytesLen = first4BytesBuff.getInt();
			}
			closeSc(readNum);
		}	

	}
	
	/**
	 * 读报文头
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @throws IOException 
	 */
	public void readHeader() throws IOException {
		if(headerBuff == null) {
			headerBuff = ByteBuffer.allocate(HeaderBytesLen);
		} 
		
		int readNum = 0;
		for(readNum = sc.read(headerBuff); readNum > 0; readNum = sc.read(headerBuff)) {
			if(headerBuff.position() == HeaderBytesLen) {
				headerBuff.flip();
				header = JsonUtil.fromJson(CharsetUtil.decoder(headerBuff), Header.class);
				System.out.println(header);
			}
			closeSc(readNum);
		}	

	}
	
	/**
	 * 读内容
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @throws IOException 
	 */
	public void readContent() throws IOException {
		if(contentBuff == null) {
			contentBuff = ByteBuffer.allocate(header.getContentLen());
		} 
		
		int readNum = 0;
		for(readNum = sc.read(contentBuff); readNum > 0; readNum = sc.read(contentBuff)) {
			if(contentBuff.position() == header.getContentLen()) {
				contentBuff.flip();
				if(header.getPackageType().equals(PackageType.TEXT)) {
					Text text = JsonUtil.fromJson(CharsetUtil.decoder(contentBuff), Text.class);
					System.out.println(text);
				}
				
				initAfterReceived();
			}
			closeSc(readNum);
		}	
	}
	
	/**
	 * 根据读取的字节数判断是否关闭当前socket channel
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public void closeSc(int readNum) throws IOException {
		//如果客户端断开连接
		if(readNum == -1) {
			System.out.println("server closed a socket");
			sc.close();
			int curConnNum = ImServer.getInstance().getConnNum()-1;
			ImServer.getInstance().setConnNum(curConnNum);
		}
	}
	
	/**
	 * 接收完之后初始化
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	private void initAfterReceived() {
		isStartReceived = false;
		first4BytesBuff = null;
		headerBuff = null;
		header = null;
		contentBuff = null;
	}
}
