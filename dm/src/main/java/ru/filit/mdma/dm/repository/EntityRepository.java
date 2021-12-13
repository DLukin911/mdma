package ru.filit.mdma.dm.repository;

import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

  static {
    enableClientCache();
    enableContactCache();
    enableAccountCache();
    enableOperationCache();
    enableAccountBalanceCache();
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
            && operation.getOperDate() <= LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow"))
            .toInstant().toEpochMilli())
        .collect(Collectors.toList());
  }

  /**
   * Получение уровня клиента за последние 30 дней по его активным счетам.
   */
/*  public ClientLevel getClientLevel(String id, Long currentDate) {
    log.info("Запрос уровня клиента за последние 30 дней по его активным счетам: {}",
        id);
    Long day30ToLong = 2592000000L;
    List<Account> accountList = getAccountByClientId(id).stream()
        .filter(account -> account.getStatus().equals(
            ACTIVE)).collect(Collectors.toList());
    if (accountList == null) {
      log.info("Активные аккаунты по данному запросу не найдены в БД");
      return null;
    }
    ClientLevel clientLevel = Low;
    List<Long> totalBalanceByEveryDay;
    for (Account account : accountList) {
      totalBalanceByEveryDay = new ArrayList<>();
      List<Operation> operationList = operationCache.stream()
          .filter(operation -> (operation.getOperDate() >= (currentDate - day30ToLong))
              && (operation.getOperDate() <= currentDate))
          .
    }

    return null;
  }*/

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
}
