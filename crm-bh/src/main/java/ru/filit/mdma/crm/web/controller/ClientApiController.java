package ru.filit.mdma.crm.web.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  public static final String REST_URL = "/client";

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

    final String requestUrl = "http://localhost:8081/dm/client/";
    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(clientSearchDto, requestHeaders);
    ResponseEntity<List<ClientDto>> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<List<ClientDto>>() {
        });
    log.info("Ответ на запрос получен: {}", responseEntity);

    return responseEntity;
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

    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(clientIdDto, requestHeaders);
    
    ResponseEntity<ClientDto> clientResponseDto = createResponseEntity(requestEntity, clientInfoUrl,
        new ParameterizedTypeReference<ClientDto>() {
        });
    ResponseEntity<List<ContactDto>> contactDtoResponseList = createResponseEntity(requestEntity,
        clientContactUrl,
        new ParameterizedTypeReference<List<ContactDto>>() {
        });
    ResponseEntity<List<AccountDto>> accountDtoResponseList = createResponseEntity(requestEntity,
        clientAccountUrl,
        new ParameterizedTypeReference<List<AccountDto>>() {
        });

    List<AccountDto> accountDtoList = accountDtoResponseList.getBody();
    for (AccountDto accountDto : accountDtoList) {
      AccountNumberDto accountNumberDto = new AccountNumberDto();
      accountNumberDto.setAccountNumber(accountDto.getNumber());
      ResponseEntity<CurrentBalanceDto> currentBalanceResponseDto = createResponseEntity(
          new HttpEntity(accountNumberDto, requestHeaders), accountBalanceUrl,
          new ParameterizedTypeReference<CurrentBalanceDto>() {
          });
      accountDto.setBalanceAmount(currentBalanceResponseDto.getBody().getBalanceAmount());
    }
    clientResponseDto.getBody().setAccounts(accountDtoList);
    clientResponseDto.getBody().setContacts(contactDtoResponseList.getBody());
    log.info("Ответ на запрос получен: {}", clientResponseDto);

    return clientResponseDto;
  }

  /**
   * Получение уровня клиента по операциям за последние 30 дней.
   */
  @PostMapping("/level")
  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    log.info("Плучение уровня клиента за последние 30 дней: {}", clientIdDto);

    final String requestUrl = "http://localhost:8081/dm/client/level";
    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(clientIdDto, requestHeaders);
    ResponseEntity<ClientLevelDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<ClientLevelDto>() {
        });
    log.info("Ответ на запрос получен: {}", responseEntity);

    return responseEntity;
  }

  /**
   * Получение информации о последних трех операциях.
   */
  @PostMapping("/account/last-operations")
  @Override
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
    log.info("Поиск последних операций по счету: {}", accountNumberDto);

    final String requestUrl = "http://localhost:8081/dm/client/account/operation";
    OperationSearchDto operationSearchDto = new OperationSearchDto();
    operationSearchDto.setAccountNumber(accountNumberDto.getAccountNumber());
    operationSearchDto.setQuantity("3");
    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(operationSearchDto, requestHeaders);
    ResponseEntity<List<OperationDto>> responseEntity = createResponseEntity(requestEntity,
        requestUrl,
        new ParameterizedTypeReference<List<OperationDto>>() {
        });
    log.info("Ответ на запрос получен: {}", responseEntity);

    return responseEntity;
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   */
  @PostMapping("/account/loan-payment")
  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    log.info("Получение суммы процентных платежей по счету Овердрафт: {}", accountNumberDto);

    final String requestUrl = "http://localhost:8081/dm/client/account/loan-payment";
    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(accountNumberDto, requestHeaders);
    ResponseEntity<LoanPaymentDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<LoanPaymentDto>() {
        });
    log.info("Ответ на запрос получен: {}", responseEntity);

    return responseEntity;
  }

  /**
   * Сохранение контакта клиента
   */
  @PostMapping("/contact/save")
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    log.info("Данные контакта для сохранения: {}", contactDto);

    final String requestUrl = "http://localhost:8081/dm/client/contact/save";
    HttpHeaders requestHeaders = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    requestHeaders.add("CRM-User-Role", authentication.getAuthorities().toString());
    requestHeaders.add("CRM-User-Name", authentication.getName());
    HttpEntity requestEntity = new HttpEntity(contactDto, requestHeaders);
    ResponseEntity<ContactDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<ContactDto>() {
        });
    log.info("Cохранение успешно. Ответ получен: {}", responseEntity);

    return responseEntity;
  }

  private <T> ResponseEntity createResponseEntity(HttpEntity requestEntity, String requestUrl,
      ParameterizedTypeReference<T> responseType) {
    URI uri = null;
    try {
      uri = new URI(requestUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    ResponseEntity<T> response = null;
    try {
      response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType);

      return response;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }
}