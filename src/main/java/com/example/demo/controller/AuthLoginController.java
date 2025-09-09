package com.example.demo.controller;

import com.example.demo.req.LoginReq;
import com.example.demo.resp.ResultUtil;
import com.example.demo.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController("/auth")
public class AuthLoginController {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login")
    public ResultUtil<Map<String, String>> login(@RequestBody LoginReq loginReq) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getAccount(), loginReq.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(loginReq.getAccount());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResultUtil.success(response);
    }

}
