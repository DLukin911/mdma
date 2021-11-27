package ru.filit.mdma.dm.repository;

import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client1;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client2;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client3;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.clientList;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.filit.oas.dm.model.Client;

@SpringBootTest
class EntityRepositoryTest {

  @Autowired
  private EntityRepository entityRepository;

  @Test
  void testShouldGetClientListToYamlDatabase() {
    List<Client> clientListFromFile = entityRepository.getClientList();
    assertTrue(clientList.size() == clientListFromFile.size());
    assertTrue(clientListFromFile.contains(client1));
    assertTrue(clientListFromFile.contains(client2));
    assertTrue(clientListFromFile.contains(client3));
  }
}