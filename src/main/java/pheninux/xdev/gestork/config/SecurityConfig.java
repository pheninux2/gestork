package pheninux.xdev.gestork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pheninux.xdev.gestork.filter.CustomAuthenticationFilter;
import pheninux.xdev.gestork.service.CustomClientDetailsService;
import pheninux.xdev.gestork.service.CustomEmployeeDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomClientDetailsService clientDetailsService;

    @Autowired
    private CustomEmployeeDetailsService employeeDetailsService;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/client/login").permitAll()
                        .requestMatchers("/server/**").hasRole("SERVER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/chef/**").hasRole("CHEF")
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/employee/login").permitAll()
                        .requestMatchers("/employee/**").hasAnyRole("SERVER", "ADMIN", "CHEF")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/client/login")
                        .loginProcessingUrl("/client/authenticate")
                        .defaultSuccessUrl("/client/home")
                        .permitAll()
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
                            if (uri.startsWith("/client/")) {
                                response.sendRedirect("/client/login");
                            } else if (uri.startsWith("/employee/")) {
                                response.sendRedirect("/employee/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        http.addFilterBefore(employeeCustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private AuthenticationFailureHandler employeeAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html");
            response.getWriter().write("<div>Invalid username or password</div>");
        };
    }

    @Bean
    public CustomAuthenticationFilter employeeCustomAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(employeeDetailsService);
        filter.setAuthenticationManager(authManager(null)); // Utiliser l'AuthenticationManager configuré
        return filter;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(employeeDetailsService)
                .passwordEncoder(passwordEncoder());
        authenticationManagerBuilder
                .userDetailsService(clientDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider(CustomEmployeeDetailsService userDetailsService) {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails == null || !passwordEncoder().matches(password, userDetails.getPassword())) {
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

