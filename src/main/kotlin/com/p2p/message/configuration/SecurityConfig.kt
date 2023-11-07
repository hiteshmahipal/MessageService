package com.p2p.message.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationEntryPoint: MyBasicAuthenticationEntryPoint
) {

    @Autowired
    private lateinit var springUserDetails: UserDetailsService

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("api/create/user").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic {
                it.authenticationEntryPoint(authenticationEntryPoint)
            }
            .sessionManagement {  }
            .sessionManagement()
            .maximumSessions(1)
            .and()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()


    @Autowired
    fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(springUserDetails)
    }
}

