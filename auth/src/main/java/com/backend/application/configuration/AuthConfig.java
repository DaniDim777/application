package com.backend.application.configuration;

import com.backend.application.exceptions.NewAccessDeniedHandler;
import com.backend.application.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(new NewAccessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/users").permitAll()
                .antMatchers("/auth/sign").permitAll()
                .antMatchers("/auth/access").permitAll()
                .antMatchers("/v2/api-docs", "/v3/api-docs", "/configuration/ui", "/swagger-resources",
                        "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/swagger-ui/**",
                        "/webjars/**", "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
