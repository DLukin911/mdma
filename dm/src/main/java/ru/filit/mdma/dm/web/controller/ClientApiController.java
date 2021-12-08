package ru.filit.mdma.dm.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dm.service.EntityService;
import ru.filit.oas.dm.web.controller.ClientApi;
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
 * Реализация API интерфейса Клиента.
 */
@Slf4j
@RestController
@RequestMapping(value = ClientApiController.REST_URL, produces = "application/json; charset=UTF-8",
    consumes = "application/json; charset=UTF-8")
public class ClientApiController implements ClientApi {

  public static final String REST_URL = "/dm/client";

  private final EntityService entityService;

  public ClientApiController(EntityService entityService) {
    this.entityService = entityService;
  }

  /**
   * Запрос списка Счетов клиента по его ID.
   */
  @PostMapping("/account")
  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto) {
    log.info("Поиск Счетов клиента по входящим данным: {}", clientIdDto.getId());

    List<AccountDto> accountDtoList = entityService.getAccount(clientIdDto);
    if (accountDtoList.isEmpty()) {
      log.info("Счета не найдены.");
      return ResponseEntity.badRequest().body(accountDtoList);
    }
    log.info("Счета успешно найдены {}", accountDtoList);

    return new ResponseEntity<>(accountDtoList, HttpStatus.OK);
  }

  /**
   * Запрос баланса по счету клиента.
   */
  @PostMapping("/account/balance")
  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto) {
    log.info("Поиск баланса клиента по входящим данным: {}", accountNumberDto);

    CurrentBalanceDto currentBalanceDto = entityService.getAccountBalance(accountNumberDto);
    if (currentBalanceDto == null) {
      log.info("Балансы по счету не найдены.");
      return ResponseEntity.badRequest().body(currentBalanceDto);
    }
    log.info("Баланс по счету успешно найден {}", currentBalanceDto);

    return new ResponseEntity<>(currentBalanceDto, HttpStatus.OK);
  }

  /**
   * Запрос Операций по счету клиента с ограничением количества вывода элементов.
   */
  @PostMapping("/account/operation")
  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto) {
    log.info("Поиск Операций счета клиента по входящим данным: {}", operationSearchDto);

    List<OperationDto> operationDtoList = entityService.getAccountOperations(operationSearchDto);
    if (operationDtoList.isEmpty()) {
      log.info("Операции по счету не найдены.");
      return ResponseEntity.badRequest().body(operationDtoList);
    }
    log.info("Операции по счету успешно найдены {}", operationDtoList);

    return new ResponseEntity<>(operationDtoList, HttpStatus.OK);
  }

  /**
   * Запрос списка Клиентов по заданным параметрам.
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto) {
    log.info("Поиск клиентов по входящим данным: {}", clientSearchDto);

    List<ClientDto> clientDtoList = entityService.getClient(clientSearchDto);
    if (clientDtoList.isEmpty()) {
      log.info("Клиенты не найдены.");
      return ResponseEntity.badRequest().body(clientDtoList);
    }
    log.info("Клиенты успешно найдены {}", clientDtoList);

    return new ResponseEntity<>(clientDtoList, HttpStatus.OK);
  }

  /**
   * POST /client/level : Получение уровня клиента
   *
   * @param clientIdDto (required)
   * @return Уровень клиента определен (status code 200) or Клиент не найден (status code 400)
   */
  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    return null;
  }

  /**
   * Запрос списка Контактов клиента по его ID.
   */
  @PostMapping("/contact")
  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto) {
    log.info("Поиск Контактов клиента по входящим данным: {}", clientIdDto.getId());

    List<ContactDto> сontactDtoList = entityService.getContact(clientIdDto);
    if (сontactDtoList.isEmpty()) {
      log.info("Контакты не найдены.");
      return ResponseEntity.badRequest().body(сontactDtoList);
    }
    log.info("Контакты успешно найдены {}", сontactDtoList);

    return new ResponseEntity<>(сontactDtoList, HttpStatus.OK);
  }

  /**
   * POST /client/account/loan-payment : Получение суммы процентных платежей по счету Овердрафт
   *
   * @param accountNumberDto (required)
   * @return Сумма процентных платежей определена (status code 200)
   */
  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    return null;
  }

  /**
   * Сохранение контакта клиента.
   */
  @PostMapping("/contact/save")
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    log.info("Сохранение контакта клиента по входящим данным: {}", contactDto);

    ContactDto contactDtoResult = entityService.saveContact(contactDto);
    if (contactDtoResult == null) {
      log.info("Такой клиент не найден.");
      return ResponseEntity.badRequest().body(contactDto);
    }
    log.info("Контакт по счету успешно сохранен {}", contactDtoResult);

    return new ResponseEntity<>(contactDtoResult, HttpStatus.OK);
  }

  /**
   * Запрос информации о Клиенте по его ID.
   */
  @PostMapping("/info")
  @Override
  public ResponseEntity<ClientDto> getClientInfo(ClientIdDto clientIdDto) {
    log.info("Поиск информации о Клиенте по входящим данным: {}", clientIdDto);

    ClientDto clientDto = entityService.getClientById(clientIdDto);
    if (clientDto == null) {
      log.info("Информация по Клиенту не найдена.");
      return ResponseEntity.badRequest().body(clientDto);
    }
    log.info("Информация по Клиенту успешно найдена {}", clientDto);

    return new ResponseEntity<>(clientDto, HttpStatus.OK);
  }
}
