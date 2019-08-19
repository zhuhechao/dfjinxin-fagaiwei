//package io.dfjinxin.common.aspect;
//
//import com.google.gson.Gson;
//import io.dfjinxin.common.utils.HttpContextUtils;
//import io.dfjinxin.common.utils.IPUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.Date;
//
//
///**
// * 系统日志，切面处理类
// *
// * @author Mark sunlightcs@gmail.com
// */
//@Aspect
//@Component
//public class ReqPermissonsAspect {
//
//	@Pointcut("@annotation(io.dfjinxin.common.annotation.RequiresPermissions)")
//	public void permissionsPointCut() {
//
//	}
//
//	@Around("permissionsPointCut()")
//	public Object around(ProceedingJoinPoint point) throws Throwable {
//		long beginTime = System.currentTimeMillis();
//		//执行方法
//		Object result = point.proceed();
//		//执行时长(毫秒)
//		long time = System.currentTimeMillis();
//
//        System.out.println("执行花费时间：" + (time - beginTime));
//
//		return result;
//	}
//
//}
