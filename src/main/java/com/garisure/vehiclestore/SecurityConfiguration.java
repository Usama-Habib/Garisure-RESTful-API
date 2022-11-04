package com.garisure.vehiclestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService)
				.passwordEncoder(getPasswordEncoder());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// RULES
		http
				.cors()
				.and()
				.csrf().disable()
				.authorizeRequests()
						.antMatchers(HttpMethod.POST,"/api/auth/signup").permitAll()
						.antMatchers("/api/users/user-management").hasAuthority("SUPER USER")
						.and()
								.formLogin();

//		http
//				.csrf().disable()
//				.authorizeRequests()
//				.antMatchers("/user-management").hasAuthority("SUPER USER")
//				.antMatchers(HttpMethod.GET, "/api/auth/signin").permitAll()
//				.antMatchers(HttpMethod.POST, "api/auth/signup").permitAll()
//				.antMatchers("/create-user").permitAll()
//				.antMatchers("/loginPage").permitAll()
//				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
//				.antMatchers("/api/auth/**").permitAll()
//				.and().formLogin()
//				.and()
//				.exceptionHandling().accessDeniedPage("/403");

		//		http
//				.csrf().disable()
//				.authorizeRequests()
//				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
//				.antMatchers("/api/auth/**").permitAll()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.httpBasic();
	}

	@Bean
	protected PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
