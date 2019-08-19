/**
 * 2019 东方金信
 */

package io.dfjinxin.common.aspect.log;
import io.dfjinxin.common.utils.UserContenUtils;
import io.dfjinxin.modules.sys.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 系统日志，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Aspect
@Component
public class SysLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysLogService sysLogService;

	@Pointcut("execution(* io.dfjinxin.modules..controller.*.*(..))")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();//请求的类
        String methodName = signature.getName();//请求的方法名

        long beginTime = System.currentTimeMillis();//开始时间
        String params = SysJsonUtils.objectsToJson(joinPoint.getArgs());//请求参数

        Object result = joinPoint.proceed();//执行方法

        String res = SysJsonUtils.objectToJson(result);//返回结果
        long time = System.currentTimeMillis() - beginTime;//执行时长(毫秒)
        LogManager.me().executeLog(LogTaskFactory.operatorLog(UserContenUtils.getUserId(), className, methodName, params, res, time));
		return result;
	}

}
