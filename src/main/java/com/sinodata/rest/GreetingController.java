package com.sinodata.rest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @RequestMapping("/")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return "启动成功";
    }
}
