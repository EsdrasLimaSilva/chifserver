package me.edu.chifserver.config;

import me.edu.chifserver.filter.FirebaseTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseTokenFilterConfig {
    @Bean
    public FilterRegistrationBean<FirebaseTokenFilter> firebaseTokenFilter(){
        var registrationBean = new FilterRegistrationBean<FirebaseTokenFilter>();
        registrationBean.setFilter(new FirebaseTokenFilter());
        registrationBean.addUrlPatterns("/");
        return registrationBean;
    }
}
