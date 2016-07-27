package org.im.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.im.commons.model.Pack;
import org.im.commons.util.RWUtil;
import org.im.imserver.ImServer;

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
	private ByteBuffer packageBuff;
	private Pack pack;
		
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
		if(pack == null) {
			pack = readPackage();
		} else {
			//1
			System.out.println(pack);
			
			//2
			init();
		}
	}
	
	/**
	 * 读取报文
	 * @author lglyoung 2016.07.26
	 * @version 1.0.0
	 */
	public Pack readPackage() {
		if(!is4BytesReceived) {
			int r = RWUtil.readFromSc(sc, first4BytesBuff);
			if(r == 1) {
				is4BytesReceived = true; 	//成功接收报文的前4个字节					
			} else if(r == -1) {			//对方的socket channel关闭
				closeSc();
			}	
		} else {
			if(packageBuff == null) {
				first4BytesBuff.flip();		//为读取做好准备
				packageBuff = ByteBuffer.allocate(first4BytesBuff.getInt());
			}
			int r = RWUtil.readFromSc(sc, packageBuff);
			if(r == 1) {
				packageBuff.flip();			//为读取做好准备
				return RWUtil.readObjFromBuff(packageBuff, Pack.class);
			} else if(r == -1) {			//对方的socket channel关闭
				closeSc();
			}
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
		packageBuff = null;
		pack = null;
	}
}
