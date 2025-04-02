package com.ptithcm.sqa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ptithcm.sqa.interceptor.RoleInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RoleInterceptor roleInterceptor;
    
    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**") // Áp dụng cho tất cả các đường dẫn
                .excludePathPatterns(
                    "/authen/**", // Loại trừ các trang xác thực
                    "/assets/**", // Loại trừ tài nguyên tĩnh
                    "/css/**", 
                    "/js/**", 
                    "/images/**",
                    "/error/**",
                    "/access-denied" // Trang thông báo từ chối truy cập
                );
    }
}