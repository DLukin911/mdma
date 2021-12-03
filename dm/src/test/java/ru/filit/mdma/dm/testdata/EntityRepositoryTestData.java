package ru.filit.mdma.dm.testdata;

import static ru.filit.oas.dm.model.Account.CurrencyEnum.RUR;
import static ru.filit.oas.dm.model.Account.StatusEnum.ACTIVE;
import static ru.filit.oas.dm.model.Account.StatusEnum.CLOSED;
import static ru.filit.oas.dm.model.Account.TypeEnum.OVERDRAFT;
import static ru.filit.oas.dm.model.Account.TypeEnum.PAYMENT;
import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;
import static ru.filit.oas.dm.model.Contact.TypeEnum.PHONE;

import java.util.ArrayList;
import java.util.List;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;

public class EntityRepositoryTestData {

  public static final Client client1 = new Client();
  public static final Client client2 = new Client();
  public static final Client client3 = new Client();
  public static final List<Client> clientList = new ArrayList<>();

  public static final Contact contactPhoneClient = new Contact();
  public static final Contact contactEmailClient = new Contact();
  public static final List<Contact> contactList = new ArrayList<>();

  public static final Account accountOneClient = new Account();
  public static final Account accountTwoClient = new Account();
  public static final List<Account> accountList = new ArrayList<>();

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

    contactPhoneClient.setId("38523");
    contactPhoneClient.setClientId("95471");
    contactPhoneClient.setType(PHONE);
    contactPhoneClient.setValue("+79161234567");

    contactEmailClient.setId("63781");
    contactEmailClient.setClientId("95471");
    contactEmailClient.setType(EMAIL);
    contactEmailClient.setValue("voroshilovke@mail.ru");

    contactList.add(contactPhoneClient);
    contactList.add(contactEmailClient);

    accountOneClient.setNumber("40817810452010063617");
    accountOneClient.setClientId("95471");
    accountOneClient.setType(PAYMENT);
    accountOneClient.setCurrency(RUR);
    accountOneClient.setStatus(CLOSED);
    accountOneClient.setOpenDate(1431648000000L);
    accountOneClient.setCloseDate(1582329600000L);

    accountTwoClient.setNumber("40817810853110005823");
    accountTwoClient.setClientId("95471");
    accountTwoClient.setType(OVERDRAFT);
    accountTwoClient.setCurrency(RUR);
    accountTwoClient.setStatus(ACTIVE);
    accountTwoClient.setOpenDate(1542326400000L);
    accountTwoClient.setDeferment(20);

    accountList.add(accountOneClient);
    accountList.add(accountTwoClient);
  }
}
