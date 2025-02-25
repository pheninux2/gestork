package pheninux.xdev.gestork.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pheninux.xdev.gestork.handler.JwtAuthenticationSuccessHandler;
import pheninux.xdev.gestork.security.filter.CustomCustomerAuthenticationFilter;
import pheninux.xdev.gestork.security.filter.CustomEmployeeAuthenticationFilter;
import pheninux.xdev.gestork.security.filter.JwtRequestFilter;
import pheninux.xdev.gestork.security.service.CustomClientDetailsService;
import pheninux.xdev.gestork.security.service.CustomEmployeeDetailsService;

import static pheninux.xdev.gestork.utils.Utils.renderAlertSingle;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomClientDetailsService customClientDetailsService;
    private final CustomEmployeeDetailsService customEmployeeDetailsService;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CustomClientDetailsService customClientDetailsService, CustomEmployeeDetailsService customEmployeeDetailsService, JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler, JwtRequestFilter jwtRequestFilter) {
        this.customClientDetailsService = customClientDetailsService;
        this.customEmployeeDetailsService = customEmployeeDetailsService;
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("all")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain customerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/view/customer/**", "/api/customer/**", "/fragment/customer/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/view/customer/login",
                                "/api/customer/authenticate").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/view/customer/login")
                        .passwordParameter("accessCode")
                        .loginProcessingUrl("/api/customer/authenticate")
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureHandler(clientAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/view/customer/logout")
                        .logoutSuccessUrl("/view/customer/login?logout")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.startsWith("/view/customer")) {
                                response.sendRedirect("/view/customer/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain employeeSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/view/**", "/api/**", "/fragment/**", "/public/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/view/employee/**", "/api/employee/**", "/public/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/view/employee/login")
                        .loginProcessingUrl("/api/employee/authenticate")
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureHandler(employeeAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/view/employee/logout")
                        .logoutSuccessUrl("/view/employee/login?logout")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.startsWith("/view/employee/")) {
                                response.sendRedirect("/view/employee/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain H2SecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/h2-console/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain StaticSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/css/**", "/js/**", "/img/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private AuthenticationFailureHandler employeeAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html");
            response.getWriter().write(renderAlertSingle("alert-danger", "Nom d'utilisateur ou mot de passe invalide."));
        };
    }

    private AuthenticationFailureHandler clientAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html");
            response.getWriter().write(renderAlertSingle("alert-danger", "Login d'utilisateur ou code d'accès invalide."));
        };
    }

    @Bean
    public CustomCustomerAuthenticationFilter clientCustomAuthenticationFilter() throws Exception {
        CustomCustomerAuthenticationFilter filter = new CustomCustomerAuthenticationFilter(customClientDetailsService);
        filter.setAuthenticationManager(authManager(null));
        return filter;
    }

    @Bean
    public CustomEmployeeAuthenticationFilter employeeCustomAuthenticationFilter() throws Exception {
        CustomEmployeeAuthenticationFilter filter = new CustomEmployeeAuthenticationFilter(customEmployeeDetailsService);
        filter.setAuthenticationManager(authManager(null));
        return filter;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customClientDetailsService)
                .passwordEncoder(passwordEncoder());
        authenticationManagerBuilder
                .userDetailsService(customEmployeeDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationProvider CustomerAuthenticationProvider(HttpServletRequest httpServletRequest) throws Exception {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();
                UserDetails userDetails;

                if (httpServletRequest.getRequestURI().startsWith("/api/employee")) {
                    userDetails = customEmployeeDetailsService.loadUserByUsername(username);
                    if (!passwordEncoder().matches(password, userDetails.getPassword())) {
                        throw new BadCredentialsException("Invalid credentials");
                    }
                } else {
                    userDetails = customClientDetailsService.loadUserByUsername(username, password);
                }
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }
}