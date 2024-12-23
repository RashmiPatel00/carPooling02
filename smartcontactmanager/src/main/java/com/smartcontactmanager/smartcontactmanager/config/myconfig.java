package com.smartcontactmanager.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class myconfig {

    // Provide your UserDetailsService implementation
    @Bean
    public UserDetailsService userDetailsService() {
        return new USerDetailsServiceImpl(); 
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").authenticated() // Protect /user/** routes
                .requestMatchers("/admin/**").hasRole("ADMIN") // Protect /admin/** routes with "ADMIN" role
                .requestMatchers("/**").permitAll() // Allow access to everything else
            )
            .formLogin(form -> form
                .permitAll().loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index") 
            )
           
            .csrf(csrf -> csrf.disable()); // Disable CSRF for development (enable for production)
        return http.build();
    }
}
