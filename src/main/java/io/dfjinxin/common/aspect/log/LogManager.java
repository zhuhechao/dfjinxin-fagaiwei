package io.dfjinxin.common.aspect.log;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志管理器
 *
 * @author zdl
 * @date 2018-10-12 15:30
 */
public class LogManager {
	
	//线程池含有线程数量
	private final int OPERATE_SIZE = 100;

	//日志记录操作延时
	private final int OPERATE_DELAY_TIME = 10;
	
	private ScheduledThreadPoolExecutor executer = new ScheduledThreadPoolExecutor(OPERATE_SIZE);
	
	private LogManager() {}
	
	public static LogManager logManager = new LogManager();
	
	public static LogManager me() {
		return logManager;
	}
	
	public void executeLog(TimerTask task) {
		executer.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}
}
