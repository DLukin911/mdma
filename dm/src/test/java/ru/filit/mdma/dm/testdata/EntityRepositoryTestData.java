package ru.filit.mdma.dm.testdata;

import java.util.ArrayList;
import java.util.List;
import ru.filit.oas.dm.model.Client;

public class EntityRepositoryTestData {

  public static final Client client1 = new Client();
  public static final Client client2 = new Client();
  public static final Client client3 = new Client();
  public static final List<Client> clientList = new ArrayList<>();

  static {
    client1.setId("95471");
    client1.setLastname("Ворошилов");
    client1.setFirstname("Клим");
    client1.setPatronymic("Ефимович");
    client1.setBirthDate(-2198880000000L);
    client1.setPassportSeries("6215");
    client1.setPassportNumber("352617");
    client1.setInn("773626104512");
    client1.setAddress(" г.Москва, ул. Ленина, дом 1");

    client2.setId("100240");
    client2.setLastname("Петров");
    client2.setFirstname("Илья");
    client2.setPatronymic("Михайлович");
    client2.setBirthDate(903657600000L);
    client2.setPassportSeries("4574");
    client2.setPassportNumber("120087");
    client2.setInn("7895415794254");

    client3.setId("234021");
    client3.setLastname("Ворошилов");
    client3.setFirstname("Лумумба");
    client3.setBirthDate(895657403600L);
    client3.setPassportSeries("3589");
    client3.setPassportNumber("100423");
    client3.setInn("2315410093219");

    clientList.add(client1);
    clientList.add(client2);
    clientList.add(client3);
  }
}
