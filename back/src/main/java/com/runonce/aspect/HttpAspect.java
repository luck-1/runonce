package com.runonce.aspect;


import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aop拦 截Controller记录日志
 *
 * @author klaus
 */
@Aspect
@Component

public class HttpAspect {

//    @Pointcut("execution(* com.runonce.controller.*.*(..))")
//
//    public void logger() {
//    }
//
//
//    @Before("logger()")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        // url
//        log.info("url={}", request.getRequestURL());
//        // method
//        log.info("method={}", request.getMethod());
//        // ip
//        log.info("ip={}", request.getRemoteAddr());
//        // 类方法
//        log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        // 参数
//        log.info("args={}", joinPoint.getArgs());
//    }
//
//    @After("logger()")
//    public void doAfter() {
//
//        log.info("222222222222");
//    }
//
//    @AfterReturning(returning = "object", pointcut = "logger()")
//    public void doAfterReturning(Object object) {
//        log.info("response={}", object.toString());
//    }


//    /* @Around是可以同时在所拦截方法的前后执行一段逻辑
//     * @param pjp
//     * @param request
//     * @throws Throwable
//     */
//    @Around("logger()")
//    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//
//
//        return null;
//    }
}