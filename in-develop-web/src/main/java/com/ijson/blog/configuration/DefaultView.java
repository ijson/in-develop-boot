package com.ijson.blog.configuration;

import com.ijson.blog.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:12 PM
 */
@Configuration
public class DefaultView implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 可添加多个
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(
                "/admin/**",
                "/auth/**",
                "/role/**",
                "/user/**",
                "/site/**"
        ).excludePathPatterns(
                "/user/edit/image");

    }
}
