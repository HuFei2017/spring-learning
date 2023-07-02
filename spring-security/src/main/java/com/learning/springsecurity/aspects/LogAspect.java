package com.learning.springsecurity.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.AttributeNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @ClassName LogAspect
 * @Description 日志切面, 第2顺序
 * @Author hufei
 * @Date 2023/4/28 13:58
 * @Version 1.0
 */
@Aspect
@Component
@Order(2)
public class LogAspect {

    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.learning..*Controller.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (null == attributes) {
            throw new AttributeNotFoundException();
        }

        HttpServletRequest request = attributes.getRequest();
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        long duration = end - start;

        String ENTER = "\n";
        String str = ENTER +
                "--------------------Method  start--------------------" + ENTER +
                "用户IP:" + request.getRemoteAddr() + ENTER +
                "请求地址:" + request.getRequestURI() + ENTER +
                "调用方法 : " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + ENTER +
                "参数: " + Arrays.toString(point.getArgs()) + ENTER +
//                "返回结果: " + result + ENTER +
                "执行时间: " + duration + "ms" + ENTER +
                "--------------------Method  End--------------------" + ENTER;
        log.info(str);

        return result;
    }
}
