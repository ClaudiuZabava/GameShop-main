package com.awbd.gameshop.config;


import com.awbd.gameshop.services.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final DataSource dataSource;

    private final JpaUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(DataSource dataSource, JpaUserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeRequests(requests -> requests

                        .requestMatchers("/","/author","/game","/category","/login").permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers("/author/update/**","/author/add","/author/delete/**").hasRole("ADMIN")
                                .requestMatchers("/game/update/**","/game/add","/game/addAuthorToGame/**",
                                        "/game/addCategGame/**","/game/delete/**").hasRole("ADMIN")
                                .requestMatchers("category/add","category/update/**","category/delete/**").hasRole("ADMIN")
                )
                .userDetailsService(userDetailsService)
                .headers((headers) -> headers.disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .loginProcessingUrl("/perform_login")
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access_denied"));
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}