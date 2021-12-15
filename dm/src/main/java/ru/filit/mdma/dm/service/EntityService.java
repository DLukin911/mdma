package ru.filit.mdma.dm.service;

import static ru.filit.mdma.dm.util.MapperUtil.convertToLocalDate;
import static ru.filit.oas.dm.model.ClientLevel.Gold;
import static ru.filit.oas.dm.model.ClientLevel.Low;
import static ru.filit.oas.dm.model.ClientLevel.Middle;
import static ru.filit.oas.dm.model.ClientLevel.Silver;
import static ru.filit.oas.dm.model.Operation.TypeEnum.EXPENSE;
import static ru.filit.oas.dm.model.Operation.TypeEnum.RECEIPT;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.repository.EntityRepository;
import ru.filit.mdma.dm.util.EqualsUtil;
import ru.filit.mdma.dm.util.MapperUtil;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.AccountBalance;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.ClientLevel;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.AccountNumberDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientIdDto;
import ru.filit.oas.dm.web.dto.ClientLevelDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.ContactDto;
import ru.filit.oas.dm.web.dto.CurrentBalanceDto;
import ru.filit.oas.dm.web.dto.LoanPaymentDto;
import ru.filit.oas.dm.web.dto.OperationDto;
import ru.filit.oas.dm.web.dto.OperationSearchDto;

/**
 * Класс для сервисных операций с сущностями.
 */
@Slf4j
@Service
public class EntityService {

  private final EntityRepository entityRepository;

  public EntityService(EntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }

  /**
   * Запрос списка сущностей Клиент через транспортный объект поиска Клиента.
   */
  public List<ClientDto> getClient(ClientSearchDto clientSearchDto) {
    log.info("Запрос списка клиентов из Entity Repository, параметры запроса: {}", clientSearchDto);

    if (clientSearchDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Client clientForSearch = MapperUtil.INSTANCE.clientForSearch(clientSearchDto);
    List<Client> clientList = entityRepository.getClientList().stream()
        .filter(client -> EqualsUtil.isEqualsClient(clientForSearch, client))
        .collect(Collectors.toList());
    if (clientList.isEmpty()) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<ClientDto> clientDtoList = new ArrayList<>();
    for (Client client : clientList) {
      clientDtoList.add(MapperUtil.INSTANCE.convert(client));
    }

    return clientDtoList;
  }

  /**
   * Запрос списка сущностей Контакты клиента по его ID.
   */
  public List<ContactDto> getContact(ClientIdDto clientIdDto) {
    log.info("Запрос списка Контактов клиентов из Entity Repository, параметры запроса: {}",
        clientIdDto);

    if (clientIdDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<Contact> contactList = entityRepository.getContactByClientId(clientIdDto.getId());
    if (contactList.isEmpty()) {
      throw new NotFoundException("По данному ID контакты не найдены.");
    }
    List<ContactDto> contactDtoList = new ArrayList<>();
    for (Contact contact : contactList) {
      contactDtoList.add(MapperUtil.INSTANCE.convert(contact));
    }

    return contactDtoList;
  }

  /**
   * Запрос списка сущностей Счетов клиента по его ID.
   */
  public List<AccountDto> getAccount(ClientIdDto clientIdDto) {
    log.info("Запрос списка Счетов клиентов из Entity Repository, параметры запроса: {}",
        clientIdDto);

    if (clientIdDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<Account> accountList = entityRepository.getAccountByClientId(clientIdDto.getId());
    if (accountList.isEmpty()) {
      throw new NotFoundException("По данному ID счета не найдены.");
    }
    List<AccountDto> accountDtoList = new ArrayList<>();
    for (Account account : accountList) {
      accountDtoList.add(MapperUtil.INSTANCE.convert(account));
    }

    return accountDtoList;
  }

  /**
   * Запрос операций по счету клиента.
   */
  public List<OperationDto> getAccountOperations(OperationSearchDto operationSearchDto) {
    log.info("Запрос операций по счету клиента из Entity Repository, параметры запроса: {}",
        operationSearchDto);

    if (operationSearchDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<Operation> operationList =
        entityRepository.getOperationListByAccountNumber(operationSearchDto.getAccountNumber(),
            operationSearchDto.getQuantity());
    if (operationList.isEmpty()) {
      throw new NotFoundException("По данному счету операции не найдены.");
    }
    List<OperationDto> operationDtoList = new ArrayList<>();
    for (Operation operation : operationList) {
      operationDtoList.add(MapperUtil.INSTANCE.convert(operation));
    }

    return operationDtoList;
  }

  /**
   * Запрос Баланса по счету Клиента.
   */
  public CurrentBalanceDto getAccountBalance(AccountNumberDto accountNumberDto) {
    log.info("Запрос баланса по счету Клиента из Entity Repository, параметры запроса: {}",
        accountNumberDto);

    if (accountNumberDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    AccountBalance accountBalance =
        entityRepository.getAccountBalance(accountNumberDto.getAccountNumber());
    if (accountBalance == null) {
      throw new NotFoundException("По данному счету информация не найдена.");
    }
    BigDecimal totalBalance = accountBalance.getAmount();
    List<Operation> operationList =
        entityRepository.getAccountOperationsLastMonth(accountNumberDto.getAccountNumber());
    for (Operation operation : operationList) {
      if (operation.getType().equals(RECEIPT)) {
        totalBalance = totalBalance.add(operation.getAmount());
      } else if (operation.getType().equals(EXPENSE)) {
        totalBalance = totalBalance.subtract(operation.getAmount());
      }
    }
    CurrentBalanceDto сurrentBalanceDto = new CurrentBalanceDto();
    сurrentBalanceDto.setBalanceAmount(new DecimalFormat("0.00")
        .format(totalBalance).replace(",", "."));

    return сurrentBalanceDto;
  }

  /**
   * Сохранение контакта клиента.
   */
  public ContactDto saveContact(ContactDto contactDto) {
    log.info("Сохранение контакта клиента в Entity Repository, параметры запроса: {}",
        contactDto);

    if (contactDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Contact contact = entityRepository.saveContact(contactDto);
    if (contact == null) {
      throw new NotFoundException("По данному клиенту информация не найдена.");
    }
    ContactDto contactDtoResult = MapperUtil.INSTANCE.convert(contact);

    return contactDtoResult;
  }

  /**
   * Получение сущности Клиента по Id.
   */
  public ClientDto getClientById(ClientIdDto clientIdDto) {
    log.info("Получение информации о Клиенте по его ID в Entity Repository, параметры запроса: {}",
        clientIdDto);

    if (clientIdDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Client client = entityRepository.getClientById(clientIdDto.getId());
    if (client == null) {
      throw new NotFoundException("По данному клиенту информация не найдена.");
    }
    ClientDto clientDto = MapperUtil.INSTANCE.convert(client);

    return clientDto;
  }

  /**
   * Получение сущности Уровень клиента по Id клиента.
   */
  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto, LocalDate dateOfEndPeriod) {
    log.info("Получение информации о счетах и среднем балансе клиента по его ID"
        + " в Entity Repository, параметры запроса: {}", clientIdDto);

    if (clientIdDto == null || dateOfEndPeriod == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Client client = entityRepository.getClientById(clientIdDto.getId());
    if (client == null) {
      throw new NotFoundException("По данному клиенту информация не найдена.");
    }
    Map<String, BigDecimal> accountMap = entityRepository.getClientLevel(client.getId(),
        dateOfEndPeriod);
    if (accountMap == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }

    String accNumber = Collections.max(accountMap.entrySet(), Map.Entry.comparingByValue())
        .getKey();
    BigDecimal avgAmount = Collections.max(accountMap.entrySet(), Map.Entry.comparingByValue())
        .getValue();

    ClientLevelDto clientLevelDto = new ClientLevelDto();
    clientLevelDto.setAccuntNumber(accNumber);
    clientLevelDto.setAvgBalance(amountWithTwoZero(avgAmount));
    clientLevelDto.setLevel(calculationClientLevel(avgAmount));

    return clientLevelDto;
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   */
  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    log.info("Получение суммы процентных платежей по счету Овердрафт,"
        + " параметры запроса: {}", accountNumberDto);

    if (accountNumberDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Account account = entityRepository.getOverdraftAccount(accountNumberDto.getAccountNumber());
    if (account == null) {
      throw new NotFoundException("Данный аккаунт не найден или не является Овердрафт аккаунтом.");
    }
    LocalDate dateOfStartPeriod = convertToLocalDate(account.getOpenDate());
    LocalDate dateOfEndPeriod;
    if (account.getCloseDate() == null) {
      dateOfEndPeriod = LocalDate.now(ZoneId.of("Europe/Moscow"));
    } else {
      dateOfEndPeriod = convertToLocalDate(account.getCloseDate());
    }
    List<Operation> operationList =
        entityRepository.getOperationListByAccountNumber(account.getNumber(), "0");
    double totalLoanPayment = 0.00;
    double lastDayBalance = 0.00;
    int deferment = account.getDeferment();
    int counter = 0;
    List<Operation> operationListByDay;
    for (LocalDate thisDay = dateOfStartPeriod; thisDay.isBefore(dateOfEndPeriod.plusDays(1));
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
      lastDayBalance += balanceByDay.doubleValue();
      if (lastDayBalance < 0 && !thisDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)
          && !thisDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        counter++;
        if (counter > deferment) {
          totalLoanPayment += Math.abs(lastDayBalance) * 0.07;
        }
      } else if (lastDayBalance >= 0) {
        counter = 0;
      }
    }

    LoanPaymentDto loanPaymentDto = new LoanPaymentDto();
    loanPaymentDto.amount(amountWithTwoZero(BigDecimal.valueOf(totalLoanPayment)));

    return loanPaymentDto;
  }

  /**
   * Подсчет уровня Клиента.
   */
  private String calculationClientLevel(BigDecimal amount) {
    Long longAmount = amount.longValue();
    ClientLevel clientLevel = Low;
    if (longAmount >= 30_000 && longAmount < 300_000) {
      clientLevel = Middle;
    } else if (longAmount >= 300_000 && longAmount < 1_000_000) {
      clientLevel = Silver;
    } else if (longAmount >= 1_000_000) {
      clientLevel = Gold;
    }

    return clientLevel.getValue();
  }

  /**
   * Преобразуем amount к двум цифрам после точки.
   */
  private String amountWithTwoZero(BigDecimal amount) {
    return new DecimalFormat("0.00").format(amount).replace(",", ".");
  }
}