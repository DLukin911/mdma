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
import ru.filit.oas.crm.web.dto.AccountDto;
import ru.filit.oas.crm.web.dto.AccountNumberDto;
import ru.filit.oas.crm.web.dto.ClientDto;
import ru.filit.oas.crm.web.dto.ClientIdDto;
import ru.filit.oas.crm.web.dto.ClientLevelDto;
import ru.filit.oas.crm.web.dto.ClientSearchDto;
import ru.filit.oas.crm.web.dto.ContactDto;
import ru.filit.oas.crm.web.dto.CurrentBalanceDto;
import ru.filit.oas.crm.web.dto.LoanPaymentDto;
import ru.filit.oas.crm.web.dto.OperationDto;
import ru.filit.oas.crm.web.dto.OperationSearchDto;

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
   * Получение информации о клиенте по его Id.
   */
  @PostMapping
  @Override
  public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
    log.info("Поиск информации о клиенте по входящим данным: {}", clientIdDto);

    final String clientInfoUrl = "http://localhost:8081/dm/client/info";
    final String clientContactUrl = "http://localhost:8081/dm/client/contact";
    final String clientAccountUrl = "http://localhost:8081/dm/client/account";
    final String accountBalanceUrl = "http://localhost:8081/dm/client/account/balance";
    URI uriClientInfo = null;
    URI uriClientContact = null;
    URI uriClientAccount = null;
    URI uriClientBalance = null;
    try {
      uriClientInfo = new URI(clientInfoUrl);
      uriClientContact = new URI(clientContactUrl);
      uriClientAccount = new URI(clientAccountUrl);
      uriClientBalance = new URI(accountBalanceUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntityIdDto = new HttpEntity(clientIdDto);
    ResponseEntity<ClientDto> responseEntityClientDto = null;
    List<ContactDto> contactDtoList = null;
    List<AccountDto> accountDtoList = null;
    CurrentBalanceDto currentBalanceDto = null;
    try {
      responseEntityClientDto = restTemplate.exchange(uriClientInfo, HttpMethod.POST,
          requestEntityIdDto, new ParameterizedTypeReference<ClientDto>() {
          }
      );
      contactDtoList = restTemplate.exchange(uriClientContact, HttpMethod.POST,
          requestEntityIdDto,
          new ParameterizedTypeReference<List<ContactDto>>() {
          }
      ).getBody();
      accountDtoList = restTemplate.exchange(uriClientAccount, HttpMethod.POST,
          requestEntityIdDto,
          new ParameterizedTypeReference<List<AccountDto>>() {
          }
      ).getBody();
      for (AccountDto accountDto : accountDtoList) {
        AccountNumberDto accountNumberDto = new AccountNumberDto();
        accountNumberDto.setAccountNumber(accountDto.getNumber());
        currentBalanceDto = restTemplate.exchange(uriClientBalance, HttpMethod.POST,
            new HttpEntity(accountNumberDto),
            new ParameterizedTypeReference<CurrentBalanceDto>() {
            }
        ).getBody();
        accountDto.setBalanceAmount(currentBalanceDto.getBalanceAmount());
      }
      responseEntityClientDto.getBody().setContacts(contactDtoList);
      responseEntityClientDto.getBody().setAccounts(accountDtoList);

      log.info("Ответ на запрос получен: {}", responseEntityClientDto);

      return responseEntityClientDto;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
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
   * Получение информации о последних трех операциях.
   */
  @PostMapping("/account/last-operations")
  @Override
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
    log.info("Поиск последних операций по счету: {}", accountNumberDto);

    final String baseUrl = "http://localhost:8081/dm/client/account/operation";
    URI uri = null;
    try {
      uri = new URI(baseUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    OperationSearchDto operationSearchDto = new OperationSearchDto();
    operationSearchDto.setAccountNumber(accountNumberDto.getAccountNumber());
    operationSearchDto.setQuantity("3");
    HttpEntity requestEntity = new HttpEntity(operationSearchDto);
    ResponseEntity<List<OperationDto>> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
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
   * Сохранение контакта клиента
   */
  @PostMapping("/contact/save")
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    log.info("Данные контакта для сохранения: {}", contactDto);

    final String baseUrl = "http://localhost:8081/dm/client/contact/save";
    URI uri = null;
    try {
      uri = new URI(baseUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(contactDto);
    ResponseEntity<ContactDto> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
          new ParameterizedTypeReference<ContactDto>() {
          }
      );
      log.info("Cохранение успешно. Ответ получен: {}", responseEntity);

      return responseEntity;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }
}