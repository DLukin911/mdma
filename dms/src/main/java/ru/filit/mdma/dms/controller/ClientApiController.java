package ru.filit.mdma.dms.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.oas.dms.web.controller.ClientApi;
import ru.filit.oas.dms.web.dto.AccountDto;
import ru.filit.oas.dms.web.dto.AccountNumberDto;
import ru.filit.oas.dms.web.dto.ClientDto;
import ru.filit.oas.dms.web.dto.ClientIdDto;
import ru.filit.oas.dms.web.dto.ClientLevelDto;
import ru.filit.oas.dms.web.dto.ClientSearchDto;
import ru.filit.oas.dms.web.dto.ContactDto;
import ru.filit.oas.dms.web.dto.CurrentBalanceDto;
import ru.filit.oas.dms.web.dto.LoanPaymentDto;
import ru.filit.oas.dms.web.dto.OperationDto;
import ru.filit.oas.dms.web.dto.OperationSearchDto;


/**
 * Реализация API интерфейса Клиента.
 */
@Slf4j
@RestController
@RequestMapping(value = ClientApiController.REST_URL, produces = "application/json; charset=UTF-8",
    consumes = "application/json; charset=UTF-8")
public class ClientApiController implements ClientApi {

  public static final String REST_URL = "/dm/client";

  /**
   * POST /client/account : Запрос счетов клиента
   */
  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    return null;
  }

  /**
   * POST /client/account/balance : Запрос баланса по счету
   */
  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    return null;
  }

  /**
   * POST /client/account/operation : Запрос операций по счету
   */
  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto, String crMUserRole, String crMUserName) {
    return null;
  }

  /**
   * POST /client : Запрос клиентов
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto,
      String crMUserRole, String crMUserName) {
    return null;
  }

  /**
   * POST /client/level : Получение уровня клиента
   */
  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    return null;
  }

  /**
   * POST /client/contact : Запрос контактов клиента
   */
  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    return null;
  }

  /**
   * POST /client/account/loan-payment : Получение суммы процентных платежей по счету Овердрафт
   */
  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    return null;
  }

  /**
   * POST /client/contact/save : Сохранение контакта клиента
   */
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto, String crMUserRole,
      String crMUserName) {
    return null;
  }
}
