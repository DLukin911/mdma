package ru.filit.mdma.dm.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.filit.oas.dm.model.Client;

class EntityRepositoryTest {

  EntityRepository entityRepository = new EntityRepository();

  @Test
  void getClient() {
    System.out.println(entityRepository.getClient().size());
    for(Client client : entityRepository.getClient()) {
      System.out.println(client);
    }
  }

  @Test
  void setClient() {
    entityRepository.setClient();
  }

}