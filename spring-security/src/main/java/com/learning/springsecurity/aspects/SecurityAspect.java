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

/**
 * @ClassName SecurityAspect
 * @Description 权限校验切面, 第1顺序, 但依旧排在spring security认证、授权之后
 * @Author hufei
 * @Date 2023/4/28 14:04
 * @Version 1.0
 */
@Aspect
@Component
@Order(1)
public class SecurityAspect {

    private final Logger log = LoggerFactory.getLogger(SecurityAspect.class);

    @Pointcut("execution(public * com.learning..*Controller.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object securityAround(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (null == attributes) {
            throw new AttributeNotFoundException();
        }

        log.info("\ninvoke method allowed.");

        return point.proceed();
    }
}
