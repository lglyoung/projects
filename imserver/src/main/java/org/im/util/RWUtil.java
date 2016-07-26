package org.im.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.im.config.PackageInfo;
import org.im.model.Package;

/**
 * 读写数据工具类
 * @author lglyoung 2016.07.25
 * @version 1.0.0
 */
public class RWUtil {
	
	/**
	 * 从信道中读取数据到缓冲区
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @param sc channel
	 * @param buffer 存放读取的数据的缓冲区
	 * @return -1：表示对方关闭了socket channel；0：表示缓冲区还没读满；1：表示缓冲区已经读满
	 */
	public static int readFromSc(SocketChannel sc, ByteBuffer buffer) {
		int readNum = 0;
		try {
			readNum = sc.read(buffer);
			while(readNum > 0) {
				if(buffer.position() == buffer.capacity()) {
					return 1;				//表示缓冲区已经读满
				}
				readNum = sc.read(buffer);
			}
			if(readNum  == -1) {
				return -1;					//表示对方关闭了socket channel
			}
		} catch(IOException e) {
			throw new RuntimeException(e);	//如果信道读取出现异常，则直接抛出异常，终止程序运行
		}
		return 0;							//表示缓冲区还没读满
	}
	
	/**
	 * 从缓冲区中读取数据，转成相应的pojo对象
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @param sc channel
	 * @param buffer 存放读取的数据的缓冲区
	 * @return -1：表示对方关闭了socket channel；0：表示缓冲区还没读满；1：表示缓冲区已经读满
	 */
	public static <T> T readObjFromBuff(ByteBuffer buff, Class<T> type) {
		T pojo = null;
		pojo = JsonUtil.fromJson(CharsetUtil.decode(buff), type);
		return pojo;
	}
	
	/**
	 * 将报文对象写入到socket channel
	 * @author lglyoung 2016.07.24
	 * @version 1.0.0
	 * @param sc channel
	 * @param buffer 存放读取的数据的缓冲区
	 * @return -1：表示对方关闭了socket channel；0：表示缓冲区还没读满；1：表示缓冲区已经读满
	 */
	public static void writeToSc(SocketChannel sc, Package pack) {
		String packStr = JsonUtil.toJson(pack);
		ByteBuffer buffer = ByteBuffer.allocate(PackageInfo.FIRST_BYTES_LEN+packStr.getBytes().length);
		buffer.putInt(packStr.getBytes().length);
		buffer.put(packStr.getBytes());
		
		//为读数据做好准备
		buffer.flip();
		while(buffer.hasRemaining()) {
			try {
				sc.write(buffer);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
	}

}
