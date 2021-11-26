package ru.filit.mdma.dm.repository;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.filit.oas.dm.model.Client;

@SpringBootTest
class EntityRepositoryTest {

  private static final Client client1 = new Client();
  private static final Client client2 = new Client();
  private static final List<Client> clientList = new ArrayList<>();

  @Autowired
  private EntityRepository entityRepository;

  @BeforeAll
  public static void setup() {
    client1.setId("95471");
    client1.setLastname("Ворошилов");
    client1.setFirstname("Клим");
    client1.setPatronymic("Ефимович");
    client1.setBirthDate(19000428L);
    client1.setPassportSeries("6215");
    client1.setPassportNumber("352617");
    client1.setInn("773626104512");
    client1.setAddress("Москва, ул. Ленина, дом 1");

    client2.setId("100240");
    client2.setLastname("Петров");
    client2.setFirstname("Илья");
    client2.setPatronymic("Михайлович");
    client2.setBirthDate(19800619L);
    client2.setPassportSeries("4574");
    client2.setPassportNumber("120087");
    client2.setInn("7895415794254");

    clientList.add(client1);
    clientList.add(client2);
  }

  @Test
  void testShouldSaveAndGetClientListToYamlDatabase() {
    entityRepository.saveClientList(clientList);
    List<Client> clientListFromFile = entityRepository.getClientList();
    assertTrue(clientList.size() == clientListFromFile.size());
    assertTrue(clientListFromFile.contains(client1));
    assertTrue(clientListFromFile.contains(client2));
  }
}