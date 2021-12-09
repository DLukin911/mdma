package ru.filit.mdma.crm.testdata;

import static ru.filit.oas.crm.model.Role.MANAGER;

import ru.filit.oas.crm.model.User;

public class UserRepositoryTestData {

  public static final User user1 = new User();

  static {
    user1.setUsername("mlsidorova");
    user1.setPassword("$2a$10$jTbRtvK58WavQ9Iy3uE3Ju.EfneZdG.XIJ0g3Qxpje3wJHAdUZvn.");
    user1.setRole(MANAGER);
  }
}