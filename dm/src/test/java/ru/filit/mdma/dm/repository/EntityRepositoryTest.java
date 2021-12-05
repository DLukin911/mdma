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
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation1;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation2;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation3;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operationList;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.dm.AbstractTest;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.AccountBalance;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;

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

  @Test
  void testShouldGetOperationListToYamlDatabase() {
    List<Operation> operationListFromFile =
        entityRepository.getOperationListByAccountNumber(operation1.getAccountNumber(), "3");
    assertTrue(operationList.size() == operationListFromFile.size());
    assertEquals(operationListFromFile.get(0).getOperDate(), operation1.getOperDate());
    assertEquals(operationListFromFile.get(1).getOperDate(), operation2.getOperDate());
    assertEquals(operationListFromFile.get(2).getOperDate(), operation3.getOperDate());
  }

  @Test
  void testShouldGetAccountBalanceToYamlDatabase() {
    AccountBalance accountBalanceFromFile =
        entityRepository.getAccountBalance("40817810452010063617");
    assertEquals(accountBalanceFromFile.getAccountNumber(), "40817810452010063617");
  }
}