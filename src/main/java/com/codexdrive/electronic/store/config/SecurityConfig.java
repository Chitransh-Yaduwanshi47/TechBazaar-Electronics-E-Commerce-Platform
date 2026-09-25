package com.codexdrive.electronic.store.config;

import com.codexdrive.electronic.store.security.JwtAuthenticationEntryPoint;
import com.codexdrive.electronic.store.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;


    // ================= PUBLIC URLS =================

    private final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/test"
    };


    // ================= SECURITY FILTER CHAIN =================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        // ================= CSRF =================

        security.csrf(csrf -> csrf.disable());


        // ================= CORS =================

        security.cors(cors ->
                cors.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public @Nullable CorsConfiguration getCorsConfiguration(
                            HttpServletRequest request) {

                        CorsConfiguration corsConfiguration =
                                new CorsConfiguration();

                        // Allowed Origins
                        corsConfiguration.setAllowedOrigins(
                                List.of(
                                        "http://localhost:4200",
                                        "http://localhost:4300"
                                )
                        );

                        // Allowed Methods
                        corsConfiguration.setAllowedMethods(
                                List.of("*")
                        );

                        // Allow Credentials
                        corsConfiguration.setAllowCredentials(true);

                        // Allowed Headers
                        corsConfiguration.setAllowedHeaders(
                                List.of("*")
                        );

                        // Cache pre-flight request
                        corsConfiguration.setMaxAge(3000L);

                        return corsConfiguration;
                    }
                })
        );


        // ================= AUTHORIZATION =================

        security.authorizeHttpRequests(request ->

                request

                        // Public URLs
                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()


                        // DELETE USER
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/users/**"
                        )
                        .hasRole(AppConstants.ROLE_ADMIN)


                        // UPDATE USER
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/users/**"
                        )
                        .hasAnyRole(
                                AppConstants.ROLE_ADMIN,
                                AppConstants.ROLE_NORMAL
                        )


                        // GET PRODUCTS - PUBLIC
                        .requestMatchers(
                                HttpMethod.GET,
                                "/products/**"
                        )
                        .permitAll()


                        // PRODUCT OTHER OPERATIONS - ADMIN
                        .requestMatchers("/products/**")
                        .hasRole(AppConstants.ROLE_ADMIN)


                        // GET USERS - PUBLIC
                        .requestMatchers(
                                HttpMethod.GET,
                                "/users/**"
                        )
                        .permitAll()


                        // CREATE USER - PUBLIC
                        .requestMatchers(
                                HttpMethod.POST,
                                "/users"
                        )
                        .permitAll()


                        // GET CATEGORIES - PUBLIC
                        .requestMatchers(
                                HttpMethod.GET,
                                "/categories/**"
                        )
                        .permitAll()


                        // CATEGORY OTHER OPERATIONS - ADMIN
                        .requestMatchers("/categories")
                        .hasRole(AppConstants.ROLE_ADMIN)


                        // GENERATE JWT TOKEN - PUBLIC
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/generate-token"
                        )
                        .permitAll()


                        // OTHER AUTH APIs
                        .requestMatchers("/auth/**")
                        .authenticated()


                        // OTHER REQUESTS
                        .anyRequest()
                        .permitAll()
        );


        // ================= EXCEPTION HANDLING =================

        security.exceptionHandling(
                ex -> ex.authenticationEntryPoint(entryPoint)
        );


        // ================= SESSION MANAGEMENT =================

        security.sessionManagement(
                session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
        );


        // ================= JWT FILTER =================

        security.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );


        return security.build();
    }


    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // ================= AUTHENTICATION MANAGER =================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}