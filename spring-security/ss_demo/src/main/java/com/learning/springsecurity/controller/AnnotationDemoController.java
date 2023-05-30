package com.learning.springsecurity.controller;

import com.learning.springsecurity.entity.UsersDO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DemoController
 * @Description TODO
 * @Author hufei
 * @Date 2023/4/27 10:55
 * @Version 1.0
 */
@RestController
public class AnnotationDemoController {

    @GetMapping("/annotation/demo")
    // 要求用户给的角色以 ROLE_ 开头, 相当于 hasRole
    @Secured({"ROLE_admin"})
    public String demo() {
        return "hello, annotation spring security";
    }

    @GetMapping("/annotation/demo/pre")
    // 可用方法: hasAuthority、hasAnyAuthority、hasRole、hasAnyRole
    @PreAuthorize("hasAuthority('admin')")
    public String preDemo() {
        return "hello, pre annotation spring security";
    }

    @GetMapping("/annotation/demo/post")
    // 可用方法: hasAuthority、hasAnyAuthority、hasRole、hasAnyRole
    @PostAuthorize("hasAuthority('admin')")
    public String postDemo() {
        return "hello, post annotation spring security";
    }

    @PostMapping("/annotation/demo/prefilter")
    // 对入参进行过滤
    @PreFilter("filterObject.username == 'A'")
    public Integer preFilterDemo(@RequestBody List<UsersDO> list) {
        return list.size();
    }

    @GetMapping("/annotation/demo/postfilter")
    // 对返回结果进行过滤
    @PostFilter("filterObject.username == 'A'")
    public List<UsersDO> postFilterDemo() {
        List<UsersDO> list = new ArrayList<>();
        list.add(new UsersDO(1, "A", "A"));
        list.add(new UsersDO(2, "B", "B"));
        return list;
    }
}
