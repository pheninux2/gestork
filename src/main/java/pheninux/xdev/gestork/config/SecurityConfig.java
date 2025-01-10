package pheninux.xdev.gestork.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import pheninux.xdev.gestork.filter.CustomClientAuthenticationFilter;
import pheninux.xdev.gestork.filter.CustomEmployeeAuthenticationFilter;
import pheninux.xdev.gestork.handler.JwtAuthenticationSuccessHandler;
import pheninux.xdev.gestork.service.CustomClientDetailsService;
import pheninux.xdev.gestork.service.CustomEmployeeDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomClientDetailsService customClientDetailsService;

    @Autowired
    private CustomEmployeeDetailsService customEmployeeDetailsService;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain CustomerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/customer/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/customer/login").permitAll()
                        .requestMatchers("/customer/**").authenticated()
                        .requestMatchers("/customer/home").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/customer/login")
                        .loginProcessingUrl("/customer/authenticate")
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureHandler(clientAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.startsWith("/client/")) {
                                response.sendRedirect("/client/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        // http.addFilterBefore(clientCustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain EmployeeSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/employee/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/server/**").hasRole("SERVER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/chef/**").hasRole("CHEF")
                        .requestMatchers("/employee/login").permitAll()
                        .requestMatchers("/employee/**").hasAnyRole("SERVER", "ADMIN", "CHEF")
                        .requestMatchers("/employee/home").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/employee/login")
                        .loginProcessingUrl("/employee/authenticate")
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureHandler(employeeAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.startsWith("/employee/")) {
                                response.sendRedirect("/employee/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        //http.addFilterBefore(employeeCustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Autoriser l'accès aux fichiers statiques
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        // Ajoutez d'autres règles de sécurité ici
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()); // Désactiver CSRF si nécessaire

        return http.build();

    }


    private AuthenticationFailureHandler employeeAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html");
            response.getWriter().write("<div>Invalid username or password</div>");
        };
    }

    private AuthenticationFailureHandler clientAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html");
            response.getWriter().write("<div>Invalid login or code access code</div>");
        };
    }

    @Bean
    public CustomClientAuthenticationFilter clientCustomAuthenticationFilter() throws Exception {
        CustomClientAuthenticationFilter filter = new CustomClientAuthenticationFilter(customClientDetailsService);
        filter.setAuthenticationManager(authManager(null)); // Utiliser l'AuthenticationManager configuré
        return filter;
    }

    @Bean
    public CustomEmployeeAuthenticationFilter employeeCustomAuthenticationFilter() throws Exception {
        CustomEmployeeAuthenticationFilter filter = new CustomEmployeeAuthenticationFilter(customEmployeeDetailsService);
        filter.setAuthenticationManager(authManager(null)); // Utiliser l'AuthenticationManager configuré
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

                if (httpServletRequest.getRequestURI().startsWith("/employee")) {
                    userDetails = customEmployeeDetailsService.loadUserByUsername(username);
                } else {
                    userDetails = customClientDetailsService.loadUserByUsername(username);
                }

                if (!passwordEncoder().matches(password, userDetails.getPassword())) {
                    throw new BadCredentialsException("Invalid credentials");
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

