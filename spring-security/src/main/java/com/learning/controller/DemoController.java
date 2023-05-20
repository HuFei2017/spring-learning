package com.learning.controller;

import com.learning.dto.DemoDTO;
import org.springframework.web.bind.annotation.*;

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
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "hello, spring security";
    }

    @PostMapping("/create")
    public Integer createEntity(@RequestBody DemoDTO request) {
        return 1;
    }

    @GetMapping("/list")
    public List<DemoDTO> listEntity() {
        return new ArrayList<>();
    }

    @DeleteMapping("/delete")
    public Integer deleteEntity(@RequestBody List<Integer> idList) {
        return 1;
    }

    @PutMapping("/update/{id}")
    public DemoDTO updateEntity(@PathVariable("id") Integer id, @RequestBody DemoDTO request) {
        return new DemoDTO();
    }
}
