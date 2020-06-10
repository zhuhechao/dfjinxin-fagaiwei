//package io.dfjinxin.common.aspect.log;
//
//import com.alibaba.fastjson.JSON;
//import io.dfjinxin.common.annotation.MyLogPointCut;
//import io.dfjinxin.common.utils.HttpContextUtils;
//import io.dfjinxin.common.utils.IPUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
///**
// * @Desc:
// * @Author: z.h.c
// * @Date: 2020/4/3 10:59
// * @Version: 1.0
// */
//
//@Aspect
//@Component
//@Slf4j
//public class MyLogAspect {
//
//    //切入点，作用在哪个方法上.这种方式需要在目标方法上标明使用切面.有特定需求的可以用这种方式
//    //用扫描包方式,可把切面为指定包下的类使用,使用方便
//    @Pointcut("@annotation(io.dfjinxin.common.annotation.MyLogPointCut)")
//    public void pointCurt() {
//    }
//
//    /**
//     * ProceedingJoinPoint 用于环绕通知,在目标方法执行前后都会执行.用于获取方法执行后的结果
//     * JoinPoint用于前/后置通知,常用于获取参数等,切入点在执行后的结果不能获取
//     * @param joinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("pointCurt()")
//    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
//        //请求方法所在类名
//        String className = joinPoint.getTarget().getClass().getName();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        //请求方法名
//        Method methodName = signature.getMethod();
//        //获取请求参数
//        Object[] args = joinPoint.getArgs();
//        String jsonArgs = JSON.toJSONString(args);
//
//        //客户端请求信息
//        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
//        String ipAddr = IPUtils.getIpAddr(httpServletRequest);
//        String origin = HttpContextUtils.getOrigin();
//        String domain = HttpContextUtils.getDomain();
//        MyLogPointCut cut = methodName.getAnnotation(MyLogPointCut.class);
//        String desc = cut.desc();
//        String name = cut.name();
//
//        //目标方法执行后的结果
//        Object result = joinPoint.proceed();
//
//        String res = SysJsonUtils.objectToJson(result);//返回结果
//        log.info("the req args:{}", jsonArgs);
//        log.info("the reqhead info- className:{},methodName:{},ipAddr:{}," +
//                        "origin:{},domain:{},cutdesc:{},cutname:{}", className
//                , methodName, ipAddr, origin, domain, desc, name);
//        log.info("the res result:{}", res);
//        return result;
//    }
//
//}
