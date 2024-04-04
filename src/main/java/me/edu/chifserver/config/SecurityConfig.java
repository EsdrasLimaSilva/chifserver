package me.edu.chifserver.config;

import me.edu.chifserver.security.FirebaseAuthenticationProvider;
import me.edu.chifserver.security.filters.FirebaseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    FirebaseAuthenticationProvider firebaseAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new FirebaseFilter(), BasicAuthenticationFilter.class)
                .authenticationProvider(firebaseAuthenticationProvider)
                .authorizeHttpRequests(req -> req.anyRequest().authenticated());

        return http.build();
    }
}
