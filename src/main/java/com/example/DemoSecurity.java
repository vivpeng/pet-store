package com.example;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Peng on 2016-09-21.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) //including @EnableWebSecurity
public class DemoSecurity extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      auth.jdbcAuthentication()
//            .dataSource(dataSource);

//      auth.ldapAuthentication()

//      auth.userDetailsService(new SomeService(someRepository));
//      SomeService must implement UserDetailsService interface with method 'loadUserByUsername()'
//      someRepository could look up the UserDetails from a relational database,
//      from a document database, from a graph database,
//      or it could just make it up.
//      SomeService doesn’t know or care what underlying data storage is used.
//      It just fetches the model object and uses it to create a UserDetails object.
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN", "USER")
                .and()
                .withUser("user").password("user").roles("USER");
    }

    @Override
    protected  void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/"/*, "/home", "/allPets", "/allCategories", "/allTags", "/tags/{searchTerm}", "/pet/findByTags"*/).permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
            .formLogin()
                //.loginPage("/login")
                //.failureUrl("/login?error")
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll();
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                //.exceptionHandling().accessDeniedPage("/access?error");
    }
}
