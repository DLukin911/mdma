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
   * POST /client/account/balance : Запрос баланса по счету
   *
   * @param accountNumberDto (required)
   * @return Баланс по счету определен (status code 200)
   */
  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto) {
    return null;
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
   * POST /client/contact/save : Сохранение контакта клиента
   *
   * @param contactDto (required)
   * @return Контакт клиента сохранен (status code 200) or Клиент не найден (status code 400)
   */
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    return null;
  }
}
