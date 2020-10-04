package com.ijson.blog.controller.view.rest;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 11:35 AM
 */

import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@Slf4j
//@RestController
//@RequestMapping("/oauth")
public class ExtRestController {


    @GetMapping(value = "/callback/qq")
    public Result login(HttpSession session, HttpServletResponse response, @RequestBody AuthContext context) {


        return Result.ok("登录成功!");
    }
}
