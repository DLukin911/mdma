package ru.filit.mdma.dm.service;

import static org.junit.Assert.assertTrue;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientDto1;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientDto3;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDto;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoNull;
import static ru.filit.mdma.dm.testdata.EntityServiceTestData.clientSearchDtoTwoEquals;

import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.web.dto.ClientDto;

@SpringBootTest
class EntityServiceTest {

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
    Assert.assertEquals(clientList.get(0).getId(), clientDto1.getId());
    Assert.assertEquals(clientList.get(1).getId(), clientDto3.getId());
  }

  @Test()
  void testShouldGetNullListClientDtoForRequestDataWhenRequestEmpty() {
    Assertions.assertThrows(NotFoundException.class,
        () -> entityService.getClient(clientSearchDtoNull));
  }
}