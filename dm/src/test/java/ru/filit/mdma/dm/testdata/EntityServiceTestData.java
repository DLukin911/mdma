package ru.filit.mdma.dm.testdata;

import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientIdDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;

public class EntityServiceTestData {

  public static final ClientSearchDto clientSearchDto = new ClientSearchDto();
  public static final ClientSearchDto clientSearchDtoTwoEquals = new ClientSearchDto();
  public static final ClientSearchDto clientSearchDtoNull = new ClientSearchDto();
  public static final ClientDto clientDto1 = new ClientDto();
  public static final ClientDto clientDto2 = new ClientDto();
  public static final ClientDto clientDto3 = new ClientDto();

  public static final ClientIdDto clientIdDto = new ClientIdDto();
  public static final ClientIdDto clientWrongIdDto = new ClientIdDto();

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
    clientDto1.setAddress("Москва, ул. Ленина, дом 1");

    clientDto2.setId("100240");
    clientDto2.setLastname("Петров");
    clientDto2.setFirstname("Илья");
    clientDto2.setPatronymic("Михайлович");
    clientDto2.setBirthDate("1998-08-21");
    clientDto2.setPassportSeries("4574");
    clientDto2.setPassportNumber("120087");
    clientDto2.setInn("7895415794254");

    clientDto3.setId("234021");
    clientDto3.setLastname("Ворошилов");
    clientDto3.setFirstname("Лумумба");
    clientDto3.setBirthDate("1998-05-20");
    clientDto3.setPassportSeries("3589");
    clientDto3.setPassportNumber("100423");
    clientDto3.setInn("2315410093219");

    clientIdDto.setId("95471");
    clientWrongIdDto.setId("777777777777");
  }
}
