package ru.filit.mdma.dm.testdata;

import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;
import static ru.filit.oas.dm.model.Contact.TypeEnum.PHONE;
import static ru.filit.oas.dm.model.Operation.TypeEnum.EXPENSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;
import ru.filit.oas.dm.web.dto.ContactDto;

public class EntityRepositoryTestData {

  public static final Client client1 = new Client();
  public static final Client client2 = new Client();
  public static final Client client3 = new Client();
  public static final List<Client> clientList = new ArrayList<>();

  public static final Contact contactPhoneClient = new Contact();
  public static final Contact contactEmailClient = new Contact();
  public static final List<Contact> contactList = new ArrayList<>();

  public static final Operation operation1 = new Operation();
  public static final Operation operation2 = new Operation();
  public static final Operation operation3 = new Operation();
  public static final List<Operation> operationList = new ArrayList<>();

  public static final ContactDto newContact = new ContactDto();
  public static final ContactDto updateContact = new ContactDto();
  public static final ContactDto noClientContact = new ContactDto();

  static {
    client1.setId("95471");
    client1.setLastname("Ворошилов");
    client1.setFirstname("Клим");
    client1.setPatronymic("Ефимович");
    client1.setBirthDate(-2198880000L);
    client1.setPassportSeries("6215");
    client1.setPassportNumber("352617");
    client1.setInn("773626104512");
    client1.setAddress(" г.Москва, ул. Ленина, дом 1");

    client2.setId("100240");
    client2.setLastname("Петров");
    client2.setFirstname("Илья");
    client2.setPatronymic("Михайлович");
    client2.setBirthDate(903657600L);
    client2.setPassportSeries("4574");
    client2.setPassportNumber("120087");
    client2.setInn("7895415794254");

    client3.setId("234021");
    client3.setLastname("Ворошилов");
    client3.setFirstname("Лумумба");
    client3.setBirthDate(895657404L);
    client3.setPassportSeries("3589");
    client3.setPassportNumber("100423");
    client3.setInn("2315410093219");

    clientList.add(client1);
    clientList.add(client2);
    clientList.add(client3);

    contactPhoneClient.setId("11744");
    contactPhoneClient.setClientId("76628");
    contactPhoneClient.setType(PHONE);
    contactPhoneClient.setValue("+79052854934");

    contactEmailClient.setId("11744");
    contactEmailClient.setClientId("76628");
    contactEmailClient.setType(EMAIL);
    contactEmailClient.setValue("danisimov@gmail.com");

    contactList.add(contactPhoneClient);
    contactList.add(contactEmailClient);

    operation1.setType(EXPENSE);
    operation1.setAccountNumber("40817810670114037905");
    operation1.setOperDate(1639094400L);
    operation1.setAmount(BigDecimal.valueOf(5645.2));
    operation1.setDescription("Амбарчик. Оплата");

    operation2.setType(EXPENSE);
    operation2.setAccountNumber("40817810670114037905");
    operation2.setOperDate(1637976600L);
    operation2.setAmount(BigDecimal.valueOf(7992.2));
    operation2.setDescription("Амбарчик. Оплата");

    operation3.setType(EXPENSE);
    operation3.setAccountNumber("40817810670114037905");
    operation3.setOperDate(1636858800L);
    operation3.setAmount(BigDecimal.valueOf(3669.6));
    operation3.setDescription("Мострансгаз. Оплата услуг");

    operationList.add(operation1);
    operationList.add(operation2);
    operationList.add(operation3);

    newContact.setClientId("95471");
    newContact.setType("PHONE");
    newContact.setValue("+79805243600");

    updateContact.setId("63781");
    updateContact.setClientId("95471");
    updateContact.setType("EMAIL");
    updateContact.setValue("pirozkov@mail.ru");

    noClientContact.setId("95471");
    noClientContact.setClientId("23232323");
    noClientContact.setType("PHONE");
    noClientContact.setValue("hi@mail.ru");
  }
}
