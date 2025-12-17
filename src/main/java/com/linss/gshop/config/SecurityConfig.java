   package com.linss.gshop.config;

   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
   import org.springframework.security.web.SecurityFilterChain;

   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {

       @Bean
       public BCryptPasswordEncoder bCryptPasswordEncoder() {
           return new BCryptPasswordEncoder();
       }

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .authorizeRequests()
                   .anyRequest().permitAll() // 允许所有请求匿名访问
                   .and()
               .csrf().disable(); // 禁用 CSRF 保护（仅在开发环境中推荐）
           return http.build();
       }
   }
   