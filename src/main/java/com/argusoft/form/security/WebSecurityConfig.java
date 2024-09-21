package com.argusoft.form.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.argusoft.form.security.filters.AuthFilter;
import com.argusoft.form.security.filters.SchemaFilter;
import com.argusoft.form.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationConfiguration configuration;

    @Bean
    public SchemaFilter schemaFilter() throws Exception {
        return new SchemaFilter(authenticationManager());
    }

    @Bean
    public AuthFilter authFilter() throws Exception {
        return new AuthFilter(authenticationManager());
    }

    @Autowired
    private UserDetailsService userDetailsService;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/register", "auth/login", "auth/logout").permitAll()
                                .anyRequest()
                                .authenticated())
                // .formLogin(Customizer.withDefaults())
                // .logout((c) -> c.logoutUrl("/logout")
                // // .logoutSuccessUrl("/logout")
                // .invalidateHttpSession(true)
                // .clearAuthentication(true).permitAll())
                // .httpBasic(Customizer.withDefaults())
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new AuthenticationFilterEntryPoint()))
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(schemaFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

}
