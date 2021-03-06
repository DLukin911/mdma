package ru.filit.mdma.dm.testdata;

import ru.filit.oas.dm.web.dto.AccessDto;
import ru.filit.oas.dm.web.dto.AccessRequestDto;
import ru.filit.oas.dm.web.dto.AccountNumberDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientIdDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.OperationDto;
import ru.filit.oas.dm.web.dto.OperationSearchDto;

public class EntityServiceTestData {

  public static final ClientSearchDto clientSearchDto = new ClientSearchDto();
  public static final ClientSearchDto clientSearchDtoTwoEquals = new ClientSearchDto();
  public static final ClientSearchDto clientSearchDtoNull = new ClientSearchDto();
  public static final ClientDto clientDto1 = new ClientDto();

  public static final ClientIdDto clientIdDto = new ClientIdDto();
  public static final ClientIdDto clientIdDto1 = new ClientIdDto();
  public static final ClientIdDto clientWrongIdDto = new ClientIdDto();

  public static final OperationSearchDto operationSearchDto = new OperationSearchDto();
  public static final OperationSearchDto operationSearchDtoWrong = new OperationSearchDto();
  public static final OperationDto operationDto1 = new OperationDto();
  public static final OperationDto operationDto2 = new OperationDto();
  public static final OperationDto operationDto3 = new OperationDto();

  public static final AccountNumberDto accountNumberDto = new AccountNumberDto();
  public static final AccountNumberDto accountNumberDtoOverdraft = new AccountNumberDto();
  public static final AccountNumberDto accountNumberDtoWrong = new AccountNumberDto();

  public static final AccessRequestDto accessRequestDtoWrong = new AccessRequestDto();
  public static final AccessRequestDto accessRequestDto = new AccessRequestDto();
  public static final AccessDto accessDto1 = new AccessDto();
  public static final AccessDto accessDto2 = new AccessDto();

  static {
    clientSearchDto.setId("95471");
    clientSearchDto.setLastname("Ворошилов");
    clientSearchDto.setFirstname("Клим");
    clientSearchDto.setPatronymic("Ефимович");
    clientSearchDto.setBirthDate("1900-04-28");
    clientSearchDto.setPassport("6215 352617");
    clientSearchDto.setInn("773626104512");

    clientSearchDtoTwoEquals.setLastname("Ворошилов");

    clientDto1.setId("95471");
    clientDto1.setLastname("Ворошилов");
    clientDto1.setFirstname("Клим");
    clientDto1.setPatronymic("Ефимович");
    clientDto1.setBirthDate("1900-04-28");
    clientDto1.setPassportSeries("6215");
    clientDto1.setPassportNumber("352617");
    clientDto1.setInn("773626104512");
    clientDto1.setAddress(" г.Москва, ул. Ленина, дом 1");

    clientIdDto.setId("80302");
    clientIdDto1.setId("95471");
    clientWrongIdDto.setId("777777777777");

    operationSearchDto.setAccountNumber("40817810670114037905");
    operationSearchDto.quantity("3");
    operationSearchDtoWrong.setAccountNumber("7777777777777777");
    operationSearchDtoWrong.quantity("3");
    operationDto1.setOperDate("2021-12-10T00:16:40");
    operationDto1.setAmount("10000.00");
    operationDto2.setOperDate("2021-12-10T00:00:00");
    operationDto2.setAmount("5645.20");
    operationDto3.setOperDate("2021-11-27T02:03:20");
    operationDto3.setAmount("10000.00");

    accountNumberDto.setAccountNumber("40817810670114037905");
    accountNumberDtoOverdraft.setAccountNumber("40817810740045024659");
    accountNumberDtoWrong.setAccountNumber("77777777777777777777");

    accessRequestDtoWrong.setRole("USER");
    accessRequestDtoWrong.setVersion("2");
    accessRequestDto.setRole("MANAGER");
    accessRequestDto.setVersion("2");

    accessDto1.setEntity("client");
    accessDto1.setProperty("id");
    accessDto2.setEntity("client");
    accessDto2.setProperty("lastname");

  }
}
