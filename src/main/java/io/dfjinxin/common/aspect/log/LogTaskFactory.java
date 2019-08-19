package io.dfjinxin.common.aspect.log;

import java.util.TimerTask;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

public class LogTaskFactory {

	private static Logger logger = LoggerFactory.getLogger(LogTaskFactory.class);

	public static TimerTask operatorLog(long userId, String className, String methodName, String parms, String result, long time) {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					logger.info("Execution Log, userId: {}, Class：{}, Method：{}, Params：{}, Result: {}, Total Time：{}", userId, className, methodName, parms, result, time);
				} catch (Exception e) {
					logger.error("保存操作日志异常：", e);
				}
			}
		};
	}
}
