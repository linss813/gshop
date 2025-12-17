package com.linss.gshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${avatar.upload.dir}")
    private String avatarUploadDir;

    @Value("${avatar.base.url}")
    private String avatarBaseUrl;

    @Value("${avatar.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 头像资源映射（/avatars/**）
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + avatarUploadDir); // 使用配置的 avatar.upload.dir

        // 普通图片资源映射（/images/**）
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/images/"); // 映射到独立的 images 子目录
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路径
                .allowedOrigins("http://localhost:8080")  // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true) // 是否允许凭证
                .maxAge(3600); // 预检请求缓存时间
    }
}
