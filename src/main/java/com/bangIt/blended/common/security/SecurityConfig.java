package com.bangIt.blended.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        .csrf(csrf -> csrf
                .ignoringRequestMatchers("/generate-order-id") // 여기에 경로 추가
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/images/**", "/error").permitAll()
                .requestMatchers("/", "/login","/logout","/partner-login","/business-registration","/bangItBot/**","/success/**", "/fail/**","/confirm/**","/generate-order-id").permitAll()
						/*
						 * .requestMatchers("/**").hasRole("PARTNER")
						 */
                		 .requestMatchers("/admin/").hasRole("USER")
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
            	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            	.logoutSuccessUrl("/login")
            	//.logoutSuccessHandler(customLogoutSuccessHandler) // 커스텀 로그아웃 핸들러 설정
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 제거
                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                .permitAll()
            );
        http.addFilterBefore(new SaveOriginalRequestFilter(), OAuth2LoginAuthenticationFilter.class);
        
        return http.build();
    }
}
