package org.im.imserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 即时通信系统服务器类
 * @author lglyoung 2016.07.23
 * @version 1.0.0
 */
public class ImServer {
	private final String IP = "127.0.0.1";		//服务器ip
	private final int PORT = 9000;				//服务器监听端口
	private Integer connNum = 0;				//当前连接数
	private ServerSocketChannel ssc;		
	private SelectionKey serverKey;
	private Selector selector;
	
	/**
	 * 内部类：连接请求处理器
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	private class AcceptHandler implements IEventHandler {
		private SelectionKey key;
		
		/**
		 * 有参构造器
		 * @author lglyoung 2016.07.24
		 * @version 1.0.0
		 */
		public AcceptHandler(SelectionKey key) {
			this.key = key;
		}

		@Override
		public void handle() throws Exception {
			ServerSocketChannel ssc = (ServerSocketChannel) serverKey.channel();
			SocketChannel sc = ssc.accept();
			sc.configureBlocking(false);
			SelectionKey acceptKey = sc.register(serverKey.selector(), SelectionKey.OP_READ);	
			connNum++;
			System.out.println(sc.getRemoteAddress().toString()+" is connecting...");
			System.out.println("current connection numbers is: "+connNum);
			
			//将读处理器绑定到key上
			IEventHandler handlerAtt = new ReadHandler(acceptKey);
			acceptKey.attach(handlerAtt);
		}
	}
	
	/**
	 * 禁止其他类创建该对象
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	private ImServer() {}
	
	/**
	 * 内部类，用来实现"懒汉式+线程安全"的单利模式
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	private static class ImServerFactory {
		private static ImServer imServer = new ImServer();
	}
	
	/**
	 * 获取IM服务器实例
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	public static ImServer getInstance() {
		return ImServerFactory.imServer;
	}
	
	/**
	 * 启动IM服务器
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	public void start() {
		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.bind(new InetSocketAddress(IP, PORT));
			serverKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
			serverKey.attach(new AcceptHandler(serverKey));
			System.out.println("imserver starts successfully...");
			
			//监听连接
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭selector
			if(selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//关闭serverSocketChannel
			if(ssc != null) {
				try {
					ssc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 监听连接
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 * @return void
	 */
	private void listen() {
		try {
			while(selector.select() > 0) {
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while(it.hasNext()) {
					SelectionKey curKey = null;
					
					//调度
					try {
						curKey = it.next();
						eventDispatcher(curKey);
						it.remove();
					} catch (Exception e) {
						if(curKey != null) {
							curKey.channel().close();
							curKey.cancel();
						}
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * I/O事件调度器
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @return void
	 * @throws Exception 
	 */
	private void eventDispatcher(SelectionKey key) throws Exception {
		IEventHandler handler = null;
		if(key.isAcceptable()) {
			handler = (AcceptHandler) key.attachment();
		} else if(key.isReadable()) {
			handler = (ReadHandler) key.attachment();
		}
		if(handler != null) {
			handler.handle();
		}
	}

	/**
	 * 停止IM服务器
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 * @return boolean 如果服务器停止成功，则返回true，否则返回false
	 */
	private boolean stop() {
		
		return false;
	}

	/**
	 * 获取IM服务器ip
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	public String getIp() {
		return IP;
	}

	/**
	 * 获取IM服务器端口
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * 获取当前连接数
	 * @author lglyoung 2016.07.23
	 * @version 1.0.0
	 */
	public Integer getConnNum() {
		return connNum;
	}

	/**
	 * 设置当前连接数
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public void setConnNum(Integer connNum) {
		this.connNum = connNum;
	}
	
}
