package com.runonce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import com.runonce.aspect.HTTPBearerAuthorizeAttribute;


@EnableFeignClients
@EnableScheduling
@EnableAsync
@SpringBootApplication
@MapperScan(basePackages ={"com.runonce.dao.eventversionone","com.runonce.dao.base","com.runonce.dao.eventversiontwo","com.runonce.dao.eventpublic","com.runonce.dao.base","com.runonce.dao.system.mapper"})
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
    * @功能描述 注册拦截器
     */
    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HTTPBearerAuthorizeAttribute());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
