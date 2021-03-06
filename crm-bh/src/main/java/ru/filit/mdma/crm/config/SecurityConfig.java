package ru.filit.mdma.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.filit.mdma.crm.service.CrmUserDetailsService;

/**
 * Настройка Security конфигурации приложения CRM-BH.
 */
@Configuration
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CrmUserDetailsService crmUserDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests().anyRequest().authenticated()
        .and().httpBasic()
        .and().sessionManagement().disable()
        .exceptionHandling()
        .authenticationEntryPoint((request, response, e) -> {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.setContentType("application/json");
          response.getWriter().write("{ \"error\": \"You are not authenticated.\" }");
        });
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(AuthenticationManagerBuilder builder)
      throws Exception {
    builder.userDetailsService(crmUserDetailsService);
  }
}