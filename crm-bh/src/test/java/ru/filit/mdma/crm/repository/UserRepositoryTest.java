package ru.filit.mdma.crm.repository;

import static org.junit.Assert.assertEquals;
import static ru.filit.mdma.crm.testdata.UserRepositoryTestData.user1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.crm.AbstractTest;
import ru.filit.oas.crm.model.User;

class UserRepositoryTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void testShouldFindUserByUserNameFromYamlDatabase() {
    User userFromDb = userRepository.findByUsername(user1.getUsername());
    assertEquals(userFromDb, user1);
  }
}