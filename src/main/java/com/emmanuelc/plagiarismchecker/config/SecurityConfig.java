package com.emmanuelc.plagiarismchecker.config;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.PrivilegeEnum;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.RoleEnum;
import com.emmanuelc.plagiarismchecker.security.JwtTokenFilter;
import com.emmanuelc.plagiarismchecker.security.MyUserDetailsServices;
import com.emmanuelc.plagiarismchecker.security.UnAuthorizedRequestsHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectMapper mapper;

    private final MyUserDetailsServices userDetailsServices;

    private final JwtTokenFilter jwtTokenFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServices);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable();

        // Disable form login and basic auth
        http.formLogin().disable().httpBasic().disable();

        // Set session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Set unauthorized requests exception handler
        http.exceptionHandling().authenticationEntryPoint(new UnAuthorizedRequestsHandler(mapper));

        http.authorizeRequests()
                .antMatchers(
                        "/h2-console/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"
                )
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users/login")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users/register")
                .hasAuthority(RoleEnum.ADMIN.toString())
                .antMatchers("/compare/**")
                .hasAuthority(PrivilegeEnum.WRITE.toString())
                .anyRequest()
                .authenticated();

        // Add our custom Token based authentication filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
