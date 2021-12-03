package ru.filit.mdma.dm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.accountList;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.accountOneClient;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.accountTwoClient;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client1;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client2;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client3;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.contactEmailClient;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.contactList;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.contactPhoneClient;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.dm.AbstractTest;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;

class EntityRepositoryTest extends AbstractTest {

  @Autowired
  private EntityRepository entityRepository;

  @Test
  void testShouldGetClientListToYamlDatabase() {
    List<Client> clientListFromFile = entityRepository.getClientList();
    assertEquals(13, clientListFromFile.size());
    assertTrue(clientListFromFile.contains(client1));
    assertTrue(clientListFromFile.contains(client2));
    assertTrue(clientListFromFile.contains(client3));
  }

  @Test
  void testShouldGetContactListByClientIdToYamlDatabase() {
    List<Contact> contactListFromFile = entityRepository.getContactByClientId(client1.getId());
    assertTrue(contactList.size() == contactListFromFile.size());
    assertTrue(contactListFromFile.contains(contactPhoneClient));
    assertTrue(contactListFromFile.contains(contactEmailClient));
  }

  @Test
  void testShouldGetAccountListByClientIdToYamlDatabase() {
    List<Account> accountListFromFile = entityRepository.getAccountByClientId(client1.getId());
    assertTrue(accountList.size() == accountListFromFile.size());
    assertTrue(accountListFromFile.contains(accountOneClient));
    assertTrue(accountListFromFile.contains(accountTwoClient));
  }
}