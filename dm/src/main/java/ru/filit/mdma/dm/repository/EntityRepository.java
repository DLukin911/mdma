package ru.filit.mdma.dm.repository;

import static ru.filit.mdma.dm.util.MapperUtil.convertToLocalDate;
import static ru.filit.oas.dm.model.Account.StatusEnum.ACTIVE;
import static ru.filit.oas.dm.model.Account.TypeEnum.OVERDRAFT;
import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.model.Access;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.AccountBalance;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Contact.TypeEnum;
import ru.filit.oas.dm.model.Operation;
import ru.filit.oas.dm.web.dto.ContactDto;

/**
 * Класс для работы с базами данных на основе YAML файлов.
 */
@Slf4j
@Repository
public class EntityRepository {

  private static int contactId = 1;

  private static Map<String, Client> clientCache = new HashMap<>();
  private static List<Contact> contactCache = new ArrayList<>();
  private static List<Account> accountCache = new ArrayList<>();
  private static List<Operation> operationCache = new ArrayList<>();
  private static List<AccountBalance> accountBalanceCache = new ArrayList<>();
  private static List<Access> access2Cache = new ArrayList<>();
  private static List<Access> access3Cache = new ArrayList<>();

  static {
    enableClientCache();
    enableContactCache();
    enableAccountCache();
    enableOperationCache();
    enableAccountBalanceCache();
    enableAccessCache();
  }

  /**
   * Получение списка сущностей Клиента.
   */
  @Cacheable("clientList")
  public List<Client> getClientList() {
    log.info("Запрос списка всех клиентов из clientCache");

    return clientCache.values().stream().collect(Collectors.toList());
  }

  /**
   * Получение сущности Клиента по Id.
   */
  public Client getClientById(String id) {
    log.info("Запрос сущности Клиент по id: {}", id);

    return clientCache.get(id);
  }

  /**
   * Получение сущности Контакта клиента по Id.
   */
  public List<Contact> getContactByClientId(String id) {
    log.info("Запрос сущности Контакт клиента по id: {}", id);

    return contactCache.stream()
        .filter(contact -> contact.getClientId().equals(id))
        .collect(Collectors.toList());
  }

  /**
   * Получение сущности Аккаунт клиента по Id.
   */
  public List<Account> getAccountByClientId(String id) {
    log.info("Запрос сущности Аккаунт клиента по id: {}", id);

    return accountCache.stream()
        .filter(contact -> contact.getClientId().equals(id))
        .collect(Collectors.toList());
  }

  /**
   * Получение списка сущностей Операции клиента по номеру счета за последний месяц.
   */
  public List<Operation> getAccountOperationsLastMonth(String accNumber) {
    log.info("Запрос списка сущностей Операции клиента по номеру счета за последний месяц: {}",
        accNumber);
    AccountBalance accountBalance = getAccountBalance(accNumber);

    if (accountBalance == null) {
      accountBalance = new AccountBalance();
      accountBalance.setAccountNumber(accNumber);
      accountBalance.setBalanceDate(Long.MIN_VALUE);
      accountBalance.setAmount(BigDecimal.valueOf(0.00));
    }

    AccountBalance finalAccountBalance = accountBalance;
    return operationCache.stream()
        .filter(operation -> operation.getAccountNumber().equals(accNumber)
            && operation.getOperDate() >= finalAccountBalance.getBalanceDate()
            && operation.getOperDate() <= LocalDateTime.now().atZone(ZoneOffset.UTC)
            .toInstant().toEpochMilli())
        .collect(Collectors.toList());
  }

  /**
   * Получение уровня и среднего баланса клиента за последние 30 дней по его активным счетам.
   */
  public Map<String, BigDecimal> getClientLevel(String id, LocalDate dateOfEndPeriod) {
    log.info("Запрос уровня клиента за последние 30 дней по его активным счетам: {}",
        id);

    List<Account> accountList = getAccountByClientId(id).stream()
        .filter(account -> account.getStatus().equals(ACTIVE)).collect(Collectors.toList());
    if (accountList.size() == 0) {
      log.info("Активные аккаунты по данному запросу не найдены в БД");
      return null;
    }
    Map<String, BigDecimal> resultMap = new HashMap<>();

    for (Account account : accountList) {
      resultMap.put(account.getNumber(), calculatingAvgAmount(account, dateOfEndPeriod));
    }

    return resultMap;
  }

  /**
   * Получение списка сущностей Операции клиента по номеру счета и ограничению по количеству
   * операций в ответе.
   */
  public List<Operation> getOperationListByAccountNumber(String accNumber, String quantity) {
    log.info("Запрос списка сущностей Операции клиента по номеру счета: {}", accNumber);

    if (quantity.equals("0")) {
      quantity = String.valueOf(Long.MAX_VALUE);
    }

    return operationCache.stream()
        .filter(operation -> operation.getAccountNumber().equals(accNumber))
        .sorted(Comparator.comparing(Operation::getOperDate).reversed())
        .limit(Long.parseLong(quantity))
        .collect(Collectors.toList());
  }

  /**
   * Получение списка сущностей Баланс счета Клиента по номеру счета.
   */
  public AccountBalance getAccountBalance(String accNumber) {
    log.info("Запрос сущности Баланс счета Клиента по номеру счета: {}", accNumber);

    return accountBalanceCache.stream()
        .filter(accountBalance -> accountBalance.getAccountNumber().equals(accNumber))
        .max(Comparator.comparingLong(AccountBalance::getBalanceDate))
        .orElse(null);
  }

  /**
   * Получение списка Access из файла по заданному номеру.
   */
  public List<Access> getAccess(String version) {
    log.info("Получение списка Access из файла по заданному номеру: {}", version);
    if (version.equals("2")) {
      return access2Cache;
    } else if (version.equals("3")) {
      return access3Cache;
    } else {
      return null;
    }
  }

  /**
   * Получение информации по счету Overdraft.
   */
  public Account getOverdraftAccount(String accNumber) {
    log.info("Получение информации по счету Overdraft: {}", accNumber);

    return accountCache.stream()
        .filter(account -> account.getNumber().equals(accNumber)
            && account.getType().equals(OVERDRAFT))
        .findAny()
        .orElse(null);
  }

  /**
   * Обновление старого Контакта клиента или создание нового, если не найдено предыдущих значений.
   */
  public Contact saveContact(ContactDto contactDto) {
    log.info("Сохранение нового Контакта Клиента: {}", contactDto);

    List<Contact> contactList = getContactByClientId(contactDto.getClientId());

    if (contactList.isEmpty()) {
      return null;
    }

    ContactDto finalContactDto = contactDto;
    Contact resultContact = contactList.stream()
        .filter(contact -> contact.getId().equals(finalContactDto.getId())).findAny().orElse(null);
    if (resultContact == null && contactDto.getId() == null) {
      resultContact = new Contact();
      resultContact.setId(String.valueOf(100_000_000 + contactId));
      contactId++;
      resultContact.setClientId(contactDto.getClientId());
      if (contactDto.getType().equals("PHONE")) {
        resultContact.setType(TypeEnum.PHONE);
      } else if (contactDto.getType().equals("EMAIL")) {
        resultContact.setType(EMAIL);
      }
      resultContact.setValue(contactDto.getValue());
      contactCache.add(resultContact);
      try {
        FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return resultContact;
    }
    if (contactDto.getType().equals("PHONE")) {
      resultContact.setType(TypeEnum.PHONE);
    } else if (contactDto.getType().equals("EMAIL")) {
      resultContact.setType(EMAIL);
    }
    resultContact.setValue(contactDto.getValue());
    Iterator<Contact> iter = contactCache.iterator();
    while (iter.hasNext()) {
      if (iter.next().getId().equals(resultContact.getId())) {
        iter.remove();
      }
    }
    contactCache.add(resultContact);
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return resultContact;
  }

  /**
   * Активация кеша сущности Клиент (id, Клиент).
   */
  private static void enableClientCache() {
    log.info("Запрос списка всех клиентов из базы данных");
    List<Client> clientList = null;
    try {
      clientList = FileUtil.getMapper()
          .readValue(FileUtil.getClientsFile(), new TypeReference<List<Client>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
    clientCache = clientList.stream().collect(Collectors.toMap(Client::getId, client -> client));
  }

  /**
   * Активация кеша сущности Контакт клиента.
   */
  private static void enableContactCache() {
    log.info("Запрос списка всех контактов клиентов из базы данных");
    try {
      contactCache = FileUtil.getMapper()
          .readValue(FileUtil.getContactsFile(), new TypeReference<List<Contact>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Активация кеша сущности Аккаунт клиента.
   */
  private static void enableAccountCache() {
    log.info("Запрос списка всех аккаунтов клиентов из базы данных");
    try {
      accountCache = FileUtil.getMapper()
          .readValue(FileUtil.getAccountsFile(), new TypeReference<List<Account>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Активация кеша сущности Операции клиента.
   */
  private static void enableOperationCache() {
    log.info("Запрос списка всех операций клиентов из базы данных");
    try {
      operationCache = FileUtil.getMapper()
          .readValue(FileUtil.getOperationsFile(), new TypeReference<List<Operation>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Активация кеша сущности Баланс на начало месяца клиента.
   */
  private static void enableAccountBalanceCache() {
    log.info("Запрос списка всех балансов клиентов из базы данных");
    try {
      accountBalanceCache = FileUtil.getMapper()
          .readValue(FileUtil.getBalancesFile(), new TypeReference<List<AccountBalance>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Активация кеша сущности Access на начало месяца клиента.
   */
  private static void enableAccessCache() {
    log.info("Запрос списка Access из базы данных");
    try {
      access2Cache = FileUtil.getMapper()
          .readValue(FileUtil.getAccess2File(), new TypeReference<List<Access>>() {
          });
      access3Cache = FileUtil.getMapper()
          .readValue(FileUtil.getAccess3File(), new TypeReference<List<Access>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Расчет среднего баланса за последние 30 дней по операциям.
   */
  private BigDecimal calculatingAvgAmount(Account account, LocalDate dateOfEndPeriod) {
    BigDecimal avgAmount = BigDecimal.ZERO;
    BigDecimal currentAccountBalance = BigDecimal.ZERO;

    LocalDate dateOfStartPeriod = dateOfEndPeriod.minusDays(30);
    LocalDate dateOpenAccount = convertToLocalDate(account.getOpenDate());
    if (dateOpenAccount.isAfter(dateOfEndPeriod)) {
      dateOfStartPeriod = convertToLocalDate(account.getOpenDate());
    }

    List<Operation> operationList =
        getOperationListByAccountNumber(account.getNumber(), "0");

    LocalDate openAccountDate = convertToLocalDate(account.getOpenDate());
    List<BigDecimal> balanceListBy30Day = new ArrayList<>();
    List<Operation> operationListByDay;
    for (LocalDate thisDay = openAccountDate; thisDay.isBefore(dateOfEndPeriod);
        thisDay = thisDay.plusDays(1)) {
      BigDecimal balanceByDay = BigDecimal.ZERO;
      LocalDate finalThisDay = thisDay;
      operationListByDay = operationList.stream()
          .filter(operation -> convertToLocalDate(operation.getOperDate()).equals(finalThisDay))
          .collect(Collectors.toList());
      for (Operation operation : operationListByDay) {
        switch (operation.getType().getValue()) {
          case ("RECEIPT"):
            balanceByDay = balanceByDay.add(operation.getAmount());
            break;
          case ("EXPENSE"):
            balanceByDay = balanceByDay.subtract(operation.getAmount());
            break;
          default:
            throw new IllegalStateException("Unexpected value: " + operation.getType());
        }
      }
      if (thisDay.isAfter(dateOfStartPeriod.minusDays(1))) {
        currentAccountBalance = currentAccountBalance.add(balanceByDay);
        balanceListBy30Day.add(currentAccountBalance);
      } else if (balanceByDay.compareTo(BigDecimal.ZERO) != 0) {
        currentAccountBalance = currentAccountBalance.add(balanceByDay);
      }
    }

    for (BigDecimal amountByDay : balanceListBy30Day) {
      avgAmount = avgAmount.add(amountByDay);
    }
    if (balanceListBy30Day.size() == 0) {
      throw new NotFoundException("Нет операций по данным датам");
    }
    avgAmount = BigDecimal.valueOf(avgAmount.doubleValue() / balanceListBy30Day.size());

    return avgAmount;
  }
}
