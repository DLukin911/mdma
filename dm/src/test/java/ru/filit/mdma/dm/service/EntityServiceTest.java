package ru.filit.mdma.dm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.newContact;
import static ru.filit.mdma.dm.testdata.EntityRepositoryTestData.noClientContact;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.accountNumberDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.accountNumberDtoWrong;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientDto1;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientIdDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientIdDto1;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoNull;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoTwoEquals;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientWrongIdDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.operationDto1;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.operationDto2;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.operationDto3;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.operationSearchDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.operationSearchDtoWrong;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.dm.AbstractTest;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientLevelDto;
import ru.filit.oas.dm.web.dto.ContactDto;
import ru.filit.oas.dm.web.dto.CurrentBalanceDto;
import ru.filit.oas.dm.web.dto.OperationDto;

class EntityServiceTest extends AbstractTest {

  @Autowired
  private EntityService entityService;

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
  void testShouldGetClientDtoForRequestData() {
    List<ClientDto> clientList = entityService.getClient(clientSearchDto);
    assertTrue(clientList.size() == 1);
    Assert.assertEquals(clientList.get(0).getId(), clientDto1.getId());
  }

  @Test
  void testShouldGetListClientDtoForRequestDataWhenTwoFieldEquals() {
    List<ClientDto> clientList = entityService.getClient(clientSearchDtoTwoEquals);
    assertTrue(clientList.size() == 2);
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestEmpty() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClient(clientSearchDtoNull));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClient(null));
  }

  @Test
  void testShouldGetContactDtoForRequestData() {
    List<ContactDto> contactDtoList = entityService.getContact(clientIdDto);
    assertTrue(contactDtoList.size() == 3);
    Assert.assertEquals(contactDtoList.get(0).getShortcut(), "6329");
    Assert.assertEquals(contactDtoList.get(1).getShortcut(), "5208");
    Assert.assertEquals(contactDtoList.get(2).getShortcut(), "d@mail.ru");
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestIdWrong() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getContact(clientWrongIdDto));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestContactNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getContact(null));
  }

  @Test
  void testShouldGetAccountDtoForRequestData() {
    List<AccountDto> accountDtoList = entityService.getAccount(clientIdDto);
    assertTrue(accountDtoList.size() == 2);
    Assert.assertEquals(accountDtoList.get(0).getShortcut(), "7905");
    Assert.assertEquals(accountDtoList.get(1).getShortcut(), "1136");
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestIdWrongAcc() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccount(clientWrongIdDto));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestContactNullAcc() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccount(null));
  }

  @Test
  void testShouldGetOperationDtoForRequestData() {
    List<OperationDto> operationDtoList = entityService.getAccountOperations(operationSearchDto);
    assertTrue(operationDtoList.size() == 3);
    assertEquals(operationDto1.getOperDate(), operationDtoList.get(0).getOperDate());
    assertEquals(operationDto1.getAmount(), operationDtoList.get(0).getAmount());
    assertEquals(operationDto2.getOperDate(), operationDtoList.get(1).getOperDate());
    assertEquals(operationDto2.getAmount(), operationDtoList.get(1).getAmount());
    assertEquals(operationDto3.getOperDate(), operationDtoList.get(2).getOperDate());
    assertEquals(operationDto3.getAmount(), operationDtoList.get(2).getAmount());
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestAccNumWrong() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccountOperations(operationSearchDtoWrong));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestOperationNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccountOperations(null));
  }

  @Test
  void testShouldGetCurrentBalanceDtoForRequestData() {
    CurrentBalanceDto currentBalanceDto = entityService.getAccountBalance(accountNumberDto);
    assertEquals("-5648.00", currentBalanceDto.getBalanceAmount());
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestAccNumDtoWrong() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccountBalance(accountNumberDtoWrong));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestAccNumNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getAccountBalance(null));
  }

  @Test
  void testShouldSaveContactForRequestData() {
    ContactDto contactDto = entityService.saveContact(newContact);
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertEquals("+79805243600", contactDto.getValue());
  }

  @Test()
  void testShouldThrowsExceptionForSaveContactWhenContactDtoNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.saveContact(null));
  }

  @Test()
  void testShouldThrowExceptionWhenSaveContactAndClientIdNotFound() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.saveContact(noClientContact));
  }

  @Test
  void testShouldGetClientByIdForRequestId() {
    assertEquals(clientDto1, entityService.getClientById(clientIdDto1));
  }

  @Test()
  void testShouldThrowsExceptionForGetClientByIdWhenRequestIdWrong() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClientById(clientWrongIdDto));
  }

  @Test()
  void testShouldThrowsExceptionForGetClientByIdWhenClientIdDtoNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClientById(null));
  }

  @Test
  void testShouldGetCurrentClientLevelDtoForRequestData() {
    ClientLevelDto clientLevelDto = entityService.getClientLevel(clientIdDto,
        LocalDate.of(2021, 12, 13));
    assertEquals("40817810670114037905", clientLevelDto.getAccuntNumber());
    assertEquals("LOW", clientLevelDto.getLevel());
    assertEquals("897.67", clientLevelDto.getAvgBalance());
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestClientWrongIdDtoWrong() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClientLevel(clientWrongIdDto, LocalDate.of(2021, 12, 13)));
  }

  @Test()
  void testShouldThrowsExceptionForRequestDataWhenRequestClientIdNull() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClientLevel(null, LocalDate.of(2021, 12, 13)));
  }
}