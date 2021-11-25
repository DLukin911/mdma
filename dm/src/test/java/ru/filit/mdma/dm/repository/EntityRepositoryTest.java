package ru.filit.mdma.dm.repository;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.filit.oas.dm.model.Client;

@SpringBootTest
class EntityRepositoryTest {

  private static final String ENTITY_REPOSITORY_SAVE_TEST_FILE =
      "src/test/resources/database/entity-repository-save-test.yaml";
  private static final String ENTITY_REPOSITORY_GET_TEST_FILE =
      "src/test/resources/database/entity-repository-get-test.yaml";

  @Autowired
  private EntityRepository entityRepository;
  private static File entityRepositorySaveTestFile = new File(ENTITY_REPOSITORY_SAVE_TEST_FILE);
  private static File entityRepositoryGetTestFile = new File(ENTITY_REPOSITORY_GET_TEST_FILE);
  private static Client client1;
  private static Client client2;
  private static List<Client> clientList;

  @BeforeAll
  public static void setup() {
    clientList = new ArrayList<>();

    client1 = new Client();
    client1.setId("95471");
    client1.setLastname("Ворошилов");
    client1.setFirstname("Клим");
    client1.setPatronymic("Ефимович");
    client1.setBirthDate(19000428L);
    client1.setPassportSeries("6215");
    client1.setPassportNumber("352617");
    client1.setInn("773626104512");
    client1.setAddress("Москва, ул. Ленина, дом 1");

    client2 = new Client();
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

  @AfterAll
  public static void cleanUp() {
    try {
      new PrintWriter(ENTITY_REPOSITORY_SAVE_TEST_FILE).close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testShouldSaveClientListToYamlDatabase() {
    entityRepository.saveClientList(clientList, entityRepositorySaveTestFile);
    List<Client> clientListFromFile = entityRepository.getClientList(entityRepositorySaveTestFile);
    assertTrue(clientList.size() == clientListFromFile.size());
    assertTrue(clientListFromFile.contains(client1));
    assertTrue(clientListFromFile.contains(client2));
  }

  @Test
  void testShouldGetClientListToYamlDatabase() {
    List<Client> clientListFromFile = entityRepository.getClientList(entityRepositoryGetTestFile);
    assertTrue(clientList.size() == clientListFromFile.size());
    assertTrue(clientListFromFile.contains(client1));
    assertTrue(clientListFromFile.contains(client2));
  }
}