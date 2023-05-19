package com.learning.aop;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @ClassName APILogAspect
 * @Description TODO
 * @Author hufei
 * @Date 2020/11/26 10:21
 * @Version 1.0
 */
@Aspect
@Component
@Order(11)
@ConditionalOnProperty(prefix = "custom.api", name = "logging", havingValue = "true")
public class ApiLogAspect {

    private final Logger log = LoggerFactory.getLogger(ApiLogAspect.class);

    @Pointcut("execution(public * com.learning..*Rest.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            log.info("{}发起请求, 请求地址:{}", request.getRemoteAddr(), request.getRequestURI());
        }
    }
}
