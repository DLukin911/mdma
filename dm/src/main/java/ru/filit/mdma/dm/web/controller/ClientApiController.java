package ru.filit.mdma.dm.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

@Slf4j
@RestController
@RequestMapping(value = ClientApiController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class ClientApiController implements ClientApi {

  public static final String REST_URL = "/dm/client";

  private static final EntityService entityService = new EntityService();

  /**
   * POST /client/account : Запрос счетов клиента
   *
   * @param clientIdDto (required)
   * @return Информации о счетах найдена (status code 200)
   */
  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto) {
    return null;
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
   * POST /client/account/operation : Запрос операций по счету
   *
   * @param operationSearchDto (required)
   * @return Операции по счету найдены (status code 200)
   */
  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto) {
    return null;
  }

  /**
   * POST /dm/client : Запрос клиентов
   * @param clientSearchDto (required)
   * @return Информации о клиенте найдена (status code 200)
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> getClient(@RequestBody ClientSearchDto clientSearchDto) {
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
   * POST /client/contact : Запрос контактов клиента
   *
   * @param clientIdDto (required)
   * @return Информации о контактах найдена (status code 200)
   */
  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto) {
    return null;
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

  @RequestMapping(value = "/hello")
  public String hello() {
    return "Hello World from Tomcat";
  }
}
