package ru.filit.mdma.crm.service;

import static org.junit.Assert.assertEquals;
import static ru.filit.mdma.crm.testdata.UserRepositoryTestData.user1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import ru.filit.mdma.crm.AbstractTest;

class CrmUserDetailsServiceTest extends AbstractTest {

  @Autowired
  private CrmUserDetailsService crmUserDetailsService;

  @Test
  void testShouldReturnValidAuthenticationDetails() {
    UserDetails userWithDetails = crmUserDetailsService.loadUserByUsername(user1.getUsername());
    assertEquals(userWithDetails.getUsername(), "mlsidorova");
    assertEquals(userWithDetails.getAuthorities().toString(), "[MANAGER]");
  }
}