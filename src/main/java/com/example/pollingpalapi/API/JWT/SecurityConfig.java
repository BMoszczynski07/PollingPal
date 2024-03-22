package com.example.pollingpalapi.API.JWT;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v2/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v2/users/register").permitAll()
                        .requestMatchers("/api/v2/polls/search-polls").permitAll()
                        .requestMatchers("/api/v2/polls/get-polls/{minusDays}").permitAll()
                        .requestMatchers("/api/v2/polls/did-user-vote/{pollId}/{userId}").permitAll()
                        .requestMatchers("/api/v2/polls/get-votes-for-user/{pollId}/{userId}").permitAll()
                        .requestMatchers("/api/v2/polls/get-votes/{pollId}").permitAll()
                        .requestMatchers("/api/v2/polls/get-poll-options/{pollId}").permitAll()
                        .anyRequest().authenticated()
                );



        return http.build();
    }
}
