package ru.filit.mdma.dm.service;

import static ru.filit.oas.dm.model.Operation.TypeEnum.EXPENSE;
import static ru.filit.oas.dm.model.Operation.TypeEnum.RECEIPT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.model.Operation;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.AccountNumberDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientIdDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.ContactDto;
import ru.filit.oas.dm.web.dto.CurrentBalanceDto;
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
    сurrentBalanceDto.setBalanceAmount(String.valueOf(totalBalance));

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
}