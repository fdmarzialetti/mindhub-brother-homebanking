package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
public class WebAuthorization{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/clients/current").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/clients/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"api/admin/loan").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/web/manager.html").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/web/account.html").hasAuthority("CLIENT")
                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/create-cards.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/card/payment").permitAll()
                .antMatchers(HttpMethod.GET,"api/loans").hasAuthority("CLIENT");


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//                res.sendRedirect("/web/login.html");
                });

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> {
//            clearAuthenticationAttributes(req);
            if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                res.setHeader("res","/web/manager.html");
            }else{
                res.setHeader("res","/web/accounts.html");
            }
        });

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Fail Login"));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }
//    private void clearAuthenticationAttributes(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//        }
//    }
}
