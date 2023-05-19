package com.learning.layoutsdk.component.tool;

import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.annotation.LayoutMethodTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LayoutComponentTool
 * @Description TODO
 * @Author hufei
 * @Date 2023/3/16 10:52
 * @Version 1.0
 */
public class LayoutComponentTool {

    private static final Logger log = LoggerFactory.getLogger(LayoutComponentTool.class);

    public static void resetParam(LayoutComponent obj, Map<String, Object[]> paramMap, Map<String, Object[]> superParamMap) {
        Map<String, Method> methodMap = new HashMap<>();
        Map<String, Method> superMethodMap = new HashMap<>();
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (!method.isAnnotationPresent(Deprecated.class) && methodName.startsWith("with")) {
                methodMap.put(methodName, method);
            }
            if (method.isAnnotationPresent(LayoutMethodTag.class)) {
                superMethodMap.put(methodName, method);
            }
        }
        doInvoke(obj, methodMap, paramMap);
        doInvoke(obj, superMethodMap, superParamMap);
    }

    private static void doInvoke(LayoutComponent obj, Map<String, Method> methodMap, Map<String, Object[]> paramMap) {
        for (Map.Entry<String, Object[]> entry : paramMap.entrySet()) {
            try {
                methodMap.get(entry.getKey()).invoke(obj, entry.getValue());
            } catch (ReflectiveOperationException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}
