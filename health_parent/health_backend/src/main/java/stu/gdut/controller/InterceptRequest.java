package stu.gdut.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InterceptRequest {
    @GetMapping("/*")
    public void common() {
    }
}
