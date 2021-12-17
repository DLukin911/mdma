package ru.filit.mdma.dm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client1;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client2;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.client3;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.contactEmailClient;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.contactPhoneClient;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.newContact;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.noClientContact;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation1;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation2;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operation3;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.operationList;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.overdraftAccount;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.updateContact;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.dm.AbstractTest;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.AccountBalance;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;

class EntityRepositoryTest extends AbstractTest {

  @Autowired
  private EntityRepository entityRepository;
  private List<Contact> contactCache;

  {
    try {
      contactCache = FileUtil.getMapper()
          .readValue(FileUtil.getContactsFile(), new TypeReference<List<Contact>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
    List<Contact> contactListFromFile = entityRepository.getContactByClientId("76628");
    assertTrue(contactListFromFile.get(0).getClientId().equals(contactPhoneClient.getClientId()));
    assertTrue(contactListFromFile.get(0).getClientId().equals(contactEmailClient.getClientId()));
  }

  @Test
  void testShouldGetAccountListByClientIdToYamlDatabase() {
    List<Account> accountListFromFile = entityRepository.getAccountByClientId("80302");
    assertEquals(2, accountListFromFile.size());
    assertEquals(accountListFromFile.get(0).getNumber(), "40817810670114037905");
    assertEquals(accountListFromFile.get(1).getNumber(), "40817810200159961136");
  }

  @Test
  void testShouldGetOperationListToYamlDatabase() {
    List<Operation> operationListFromFile =
        entityRepository.getOperationListByAccountNumber("40817810670114037905", "3");
    assertTrue(operationList.size() == operationListFromFile.size());
    assertEquals(operationListFromFile.get(0).getOperDate(), operation1.getOperDate());
    assertEquals(operationListFromFile.get(1).getOperDate(), operation2.getOperDate());
    assertEquals(operationListFromFile.get(2).getOperDate(), operation3.getOperDate());
  }

  @Test
  void testShouldGetAccountBalanceToYamlDatabase() {
    AccountBalance accountBalanceFromFile =
        entityRepository.getAccountBalance("40817810670114037905");
    assertEquals(BigDecimal.valueOf(-10002.8), accountBalanceFromFile.getAmount());
  }

  @Test
  void testShouldSaveContactToYamlDatabase() {
    entityRepository.saveContact(newContact);
    Contact contact = entityRepository.getContactByClientId(newContact.getClientId()).stream()
        .filter(contact1 -> contact1.getId().equals("100000001")).findAny().orElse(null);
    assertEquals("100000001", contact.getId());
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testShouldUpdateContactToYamlDatabase() {
    entityRepository.saveContact(updateContact);
    Contact contact = entityRepository.getContactByClientId(newContact.getClientId()).stream()
        .filter(contact1 -> contact1.getId().equals("63781")).findAny().orElse(null);
    assertEquals("pirozkov@mail.ru", contact.getValue());
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testShouldGetAccountAndAvgBalanceFromYamlDatabase() {
    assertEquals("{40817810670114037905=-381.97333333333336,"
            + " 40817810200159961136=351285.83999999997}",
        entityRepository.getClientLevel("80302", LocalDate.of(2021, 12, 13))
            .toString());
  }

  @Test
  void testShouldReturnNullWhenClientIdNotFoundByClientLevel() {
    assertEquals(null, entityRepository.getClientLevel("777777",
        LocalDate.of(2021, 12, 13)));
  }

  @Test
  void testShouldReturnNullWhenClientIdNotFound() {
    assertEquals(null, entityRepository.saveContact(noClientContact));
  }

  @Test
  void testShouldGetClientByIdFromYamlDatabase() {
    assertEquals(client1, entityRepository.getClientById(client1.getId()));
  }

  @Test
  void testShouldReturnNullWhenGetClientByIdAndClientIdWrongId() {
    assertEquals(null, entityRepository.getClientById("7777777"));
  }

  @Test
  void testShouldReturnNullWhenGetClientByIdAndClientIdDtoNull() {
    assertEquals(null, entityRepository.getClientById(null));
  }

  @Test
  void testShouldGetAccountOverdraftFromYamlDatabase() {
    assertEquals(overdraftAccount,
        entityRepository.getOverdraftAccount(overdraftAccount.getNumber()));
  }

  @Test
  void testShouldReturnNullWhenAccountOverdraftNotFound() {
    assertEquals(null, entityRepository.getOverdraftAccount("444444444444"));
  }
}