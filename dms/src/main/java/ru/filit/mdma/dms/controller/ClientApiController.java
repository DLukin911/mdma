package ru.filit.mdma.dms.controller;

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
import ru.filit.mdma.dms.util.exception.NotFoundException;
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

  public static final String REST_URL = "/client";

  private final RestTemplate restTemplate;

  @Autowired
  public ClientApiController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Запрос списка Счетов клиента по его ID.
   */
  @PostMapping("/account")
  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Поиск Счетов клиента по входящим данным: {}", clientIdDto.getId());

    final String clientAccountUrl = "http://localhost:8082/dm/client/account";

    URI uri = null;
    try {
      uri = new URI(clientAccountUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<List<AccountDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<AccountDto>>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Запрос баланса по счету клиента.
   */
  @PostMapping("/account/balance")
  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск баланса клиента по входящим данным: {}", accountNumberDto);
    final String accountBalanceUrl = "http://localhost:8082/dm/client/account/balance";

    URI uri = null;
    try {
      uri = new URI(accountBalanceUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(accountNumberDto);
    ResponseEntity<CurrentBalanceDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<CurrentBalanceDto>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Запрос Операций по счету клиента с ограничением количества вывода элементов.
   */
  @PostMapping("/account/operation")
  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto, String crMUserRole, String crMUserName) {
    log.info("Поиск Операций счета клиента по входящим данным: {}", operationSearchDto);
    final String accountOperationUrl = "http://localhost:8082/dm/client/account/operation";

    URI uri = null;
    try {
      uri = new URI(accountOperationUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(operationSearchDto);
    ResponseEntity<List<OperationDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<OperationDto>>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Запрос списка Клиентов по заданным параметрам.
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск клиентов по входящим данным: {}", clientSearchDto);
    final String clientListUrl = "http://localhost:8082/dm/client/";

    URI uri = null;
    try {
      uri = new URI(clientListUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientSearchDto);
    ResponseEntity<List<ClientDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
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
   * Получение уровня клиента.
   */
  @PostMapping("/level")
  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Получение уровня клиента по входящим данным: {}", clientIdDto);
    final String clientLevelUrl = "http://localhost:8082/dm/client/level";

    URI uri = null;
    try {
      uri = new URI(clientLevelUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<ClientLevelDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<ClientLevelDto>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Запрос списка Контактов клиента по его ID.
   */
  @PostMapping("/contact")
  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Поиск Контактов клиента по входящим данным: {}", clientIdDto.getId());

    final String accountContactUrl = "http://localhost:8082/dm/client/contact";

    URI uri = null;
    try {
      uri = new URI(accountContactUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<List<ContactDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<ContactDto>>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   */
  @PostMapping("/account/loan-payment")
  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск суммы процентных платежей по счету Овердрафт"
        + " по входящим данным: {}", accountNumberDto);
    final String clientLoanPaymentUrl = "http://localhost:8082/dm/client/account/loan-payment";

    URI uri = null;
    try {
      uri = new URI(clientLoanPaymentUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(accountNumberDto);
    ResponseEntity<LoanPaymentDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<LoanPaymentDto>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Сохранение контакта клиента.
   */
  @PostMapping("/contact/save")
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto, String crMUserRole,
      String crMUserName) {
    log.info("Сохранение контакта клиента по входящим данным: {}", contactDto);
    final String clientSaveContactUrl = "http://localhost:8082/dm/client/contact/save";

    URI uri = null;
    try {
      uri = new URI(clientSaveContactUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(contactDto);
    ResponseEntity<ContactDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<ContactDto>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }

  /**
   * Запрос информации о Клиенте по его ID.
   */
  @PostMapping("/info")
  @Override
  public ResponseEntity<ClientDto> getClientInfo(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Поиск информации о Клиенте по входящим данным: {}", clientIdDto);

    final String clientInfoUrl = "http://localhost:8082/dm/client/info";

    URI uri = null;
    try {
      uri = new URI(clientInfoUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<ClientDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<ClientDto>() {
          }
      );

      log.info("Ответ на запрос получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }
}
