package ru.filit.mdma.dm.testdata;

import static ru.filit.oas.dm.model.Account.CurrencyEnum.RUR;
import static ru.filit.oas.dm.model.Account.StatusEnum.ACTIVE;
import static ru.filit.oas.dm.model.Account.StatusEnum.CLOSED;
import static ru.filit.oas.dm.model.Account.TypeEnum.OVERDRAFT;
import static ru.filit.oas.dm.model.Account.TypeEnum.PAYMENT;
import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;
import static ru.filit.oas.dm.model.Contact.TypeEnum.PHONE;
import static ru.filit.oas.dm.model.Operation.TypeEnum.EXPENSE;
import static ru.filit.oas.dm.model.Operation.TypeEnum.RECEIPT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;

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

  public static final Operation operation1 = new Operation();
  public static final Operation operation2 = new Operation();
  public static final Operation operation3 = new Operation();
  public static final List<Operation> operationList = new ArrayList<>();

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

    operation1.setType(RECEIPT);
    operation1.setAccountNumber("40817810853110005823");
    operation1.setOperDate(1634646960000L);
    operation1.setAmount(BigDecimal.valueOf(5000.00));
    operation1.setDescription("VSP 5311 MOSKVA RUS");

    operation2.setType(EXPENSE);
    operation2.setAccountNumber("40817810853110005823");
    operation2.setOperDate(1634062500000L);
    operation2.setAmount(BigDecimal.valueOf(4926.59));
    operation2.setDescription("YM OZON 1 GOROD MOSKVA RUS");

    operation3.setType(EXPENSE);
    operation3.setAccountNumber("40817810853110005823");
    operation3.setOperDate(1631202000000L);
    operation3.setAmount(BigDecimal.valueOf(215.00));
    operation3.setDescription("МУП Водоканал. Оплата услуг");

    operationList.add(operation1);
    operationList.add(operation2);
    operationList.add(operation3);
  }
}
