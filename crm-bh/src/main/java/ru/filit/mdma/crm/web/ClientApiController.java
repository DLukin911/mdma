package ru.filit.mdma.crm.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.filit.mdma.crm.util.exception.NotFoundException;
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

  private final RestTemplate restTemplate;

  @Autowired
  public ClientApiController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Поиск Клиентов по заданному запросу.
   */
  @PostMapping("/find")
  @Override
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
    log.info("Поиск клиентов по входящим данным: {}", clientSearchDto);

    final String baseUrl = "http://localhost:8081/dm/client/";
    URI uri = null;
    try {
      uri = new URI(baseUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientSearchDto);
    ResponseEntity<List<ClientDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
          new ParameterizedTypeReference<List<ClientDto>>() {
          }
      );
      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
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