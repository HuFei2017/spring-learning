package com.learning.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.AttributeNotFoundException;
import java.util.Arrays;

/**
 * @ClassName APILogAspect
 * @Description TODO
 * @Author hufei
 * @Date 2020/11/26 10:21
 * @Version 1.0
 */
@Aspect
@Component
@Order(10)
@ConditionalOnProperty(prefix = "custom.web", name = "logging", havingValue = "true")
public class WebLogAspect {

    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.learning..*Controller.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

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
