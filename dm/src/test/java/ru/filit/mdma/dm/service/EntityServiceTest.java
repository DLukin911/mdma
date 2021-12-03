package ru.filit.mdma.dm.service;

import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientDto1;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientIdDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoNull;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoTwoEquals;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientWrongIdDto;

import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.filit.mdma.dm.AbstractTest;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ContactDto;

class EntityServiceTest extends AbstractTest {

  @Autowired
  private EntityService entityService;

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
    assertTrue(contactDtoList.size() == 2);
    Assert.assertEquals(contactDtoList.get(0).getShortcut(), "4567");
    Assert.assertEquals(contactDtoList.get(1).getShortcut(), "e@mail.ru");
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
    Assert.assertEquals(accountDtoList.get(0).getShortcut(), "3617");
    Assert.assertEquals(accountDtoList.get(1).getShortcut(), "5823");
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
}