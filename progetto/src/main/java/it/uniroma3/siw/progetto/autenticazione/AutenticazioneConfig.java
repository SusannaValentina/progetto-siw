package it.uniroma3.siw.progetto.autenticazione;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.siw.progetto.model.Credenziali;

@Configuration
@EnableWebSecurity
public class AutenticazioneConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/","/index","/login","/registra", "/css/**").permitAll()
		.antMatchers(HttpMethod.POST, "/login","/registra").permitAll()
		.antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credenziali.RUOLO_ADMIN)
		.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credenziali.RUOLO_ADMIN)
		.anyRequest().authenticated()
		.and().formLogin().defaultSuccessUrl("/home")
		.and().oauth2Login().defaultSuccessUrl("/home")
		.and().logout().logoutUrl("/logout").logoutSuccessUrl("/index")
		.invalidateHttpSession(true)
		.clearAuthentication(true).permitAll();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.authoritiesByUsernameQuery("SELECT username, ruolo FROM credenziali WHERE username=?")
		.usersByUsernameQuery("SELECT username, password, 1 as enable FROM credenziali WHERE username=?");
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
