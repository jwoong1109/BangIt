package com.bangIt.blended.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/images/**", "/error").permitAll()
                .requestMatchers("/", "/login").permitAll()
                .requestMatchers("/partner/**").hasRole("PARTNER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .successHandler(customAuthenticationSuccessHandler)
            )
            .logout(logout -> logout
            	.logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 제거
                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
               
            );
        

        return http.build();
    }
}
