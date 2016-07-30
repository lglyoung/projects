package org.im.commons.handler;

import org.im.application.AbstractApplication;

/**
 * 接口:事件处理器
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public interface IEventHandler {
	public void handle() throws Exception;
	public AbstractApplication getApp();
	public void setApp(AbstractApplication app);
}
