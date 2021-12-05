package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import ru.filit.oas.dm.model.Operation;

/**
 * Класс для работы с базами данных на основе YAML файлов.
 */
@Slf4j
@Repository
public class EntityRepository {

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

    return clientCache.values().stream().toList();
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
        .findAny()
        .orElse(null);
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
