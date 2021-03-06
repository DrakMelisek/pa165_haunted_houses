/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peta2kuba.pa165_haunted_houses.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author zelva
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/ability/add").authenticated()
                .antMatchers("/ability/edit/**").authenticated()
                .antMatchers("/ability/delete/**").authenticated()
                .antMatchers("/person/add").authenticated()
                .antMatchers("/person/edit/**").authenticated()
                .antMatchers("/person/delete/**").authenticated()
                .antMatchers("/house/add").authenticated()
                .antMatchers("/house/edit/**").authenticated()
                .antMatchers("/house/delete/**").authenticated()
                .antMatchers("/haunter/add").authenticated()
                .antMatchers("/haunter/edit/**").authenticated()
                .antMatchers("/haunter/delete/**").authenticated()
                .and().formLogin().loginPage("/login")
                .usernameParameter("email").passwordParameter("password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/home")
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/denied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

}
