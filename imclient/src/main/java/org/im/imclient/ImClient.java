package org.im.imclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 即时通信系统客户端类
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class ImClient {
	private Selector selector;
	private SocketChannel sc;							//客户端socket
	private Charset charset = Charset.forName("UTF-8");	
	private static ImClient imClient = new ImClient();
	
	/**
	 * 私有的构造器
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	private ImClient() {}
	
	/**
	 * 获取客户端实例
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public static ImClient getInstance() {
		return imClient;
	}
	
	/**
	 * 启动
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @throws IOException 
	 */
	public void start(String serverIp, int serverport) throws IOException {
		selector = Selector.open();
		sc = SocketChannel.open(new InetSocketAddress(serverIp, serverport));
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_CONNECT);
	}
	
	/**
	 * 停止
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @throws IOException 
	 */
	public void stop() throws Exception {
		if(selector != null) {
			selector.close();
		}
		if(sc != null) {
			sc.close();
		}
	}

	/**
	 * 获取客户端socket channel
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 */
	public SocketChannel getSc() {
		return sc;
	}

}
