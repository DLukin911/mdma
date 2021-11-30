package ru.filit.mdma.crm.web;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.crm.config.RestTemplateConfig;
import ru.filit.oas.crm.web.controller.ClientApi;
import ru.filit.oas.crm.web.dto.AccountNumberDto;
import ru.filit.oas.crm.web.dto.ClientDto;
import ru.filit.oas.crm.web.dto.ClientIdDto;
import ru.filit.oas.crm.web.dto.ClientLevelDto;
import ru.filit.oas.crm.web.dto.ClientSearchDto;
import ru.filit.oas.crm.web.dto.ContactDto;
import ru.filit.oas.crm.web.dto.LoanPaymentDto;
import ru.filit.oas.crm.web.dto.OperationDto;

/**
 * Реализация API интерфейса Клиента.
 */
@Slf4j
@RestController
@RequestMapping(value = ClientApiController.REST_URL, produces = "application/json; charset=UTF-8",
    consumes = "application/json; charset=UTF-8")
public class ClientApiController implements ClientApi {

  public static final String REST_URL = "/api/client";

  private final RestTemplateConfig restTemplate;

  public ClientApiController(
      RestTemplateConfig restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * POST /client/find : Поиск клиентов
   *
   * @param clientSearchDto (required)
   * @return Поиск клиентов выполнен (status code 200)
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {

    return null;
  }

  /**
   * POST /client : Получение информации о клиенте
   *
   * @param clientIdDto clientId (required)
   * @return Информации о клиенте найдена (status code 200) or Клиент не найден (status code 400)
   */
  @Override
  public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
    return null;
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
   * POST /client/account/last-operations : Получение информации о последних операциях
   *
   * @param accountNumberDto (required)
   * @return Информации о последних операциях найдена (status code 200)
   */
  @Override
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
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
}