package ru.filit.mdma.crm.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.filit.mdma.crm.repository.UserRepository;
import ru.filit.oas.crm.model.User;

/**
 * Класс службы аутентификации.
 */
@Component
public class CrmUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("Пользователь не найден.");
    }

    String userRole = user.getRole().getValue();
    List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(userRole));

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(), authorities);
  }
}