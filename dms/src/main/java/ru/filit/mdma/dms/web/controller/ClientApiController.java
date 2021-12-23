package ru.filit.mdma.dms.web.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.filit.mdma.dms.repository.AccessRepository;
import ru.filit.mdma.dms.service.TokenCacheService;
import ru.filit.mdma.dms.util.DataMask;
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
  private final AccessRepository accessRepository;
  private final TokenCacheService tokenCacheService;

  @Autowired
  public ClientApiController(AccessRepository accessRepository, RestTemplate restTemplate,
      TokenCacheService tokenCacheService) {
    this.accessRepository = accessRepository;
    this.restTemplate = restTemplate;
    this.tokenCacheService = tokenCacheService;
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
    List<AccountDto> accountDtoList = null;
    try {
      accountDtoList = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<AccountDto>>() {
          }
      ).getBody();
      List<AccountDto> finalAccountDtoList = new ArrayList<>();
      for (AccountDto accountDto : accountDtoList) {
        finalAccountDtoList.add((AccountDto) DataMask.mask(accountDto, crMUserRole,
            "account", accessRepository, tokenCacheService).getBody());
      }

      return new ResponseEntity<>(finalAccountDtoList, HttpStatus.OK);
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

      return DataMask.mask(responseEntity.getBody(), crMUserRole, "dummy",
          accessRepository, tokenCacheService);
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
    List<OperationDto> operationDtoList = null;
    try {
      operationDtoList = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<OperationDto>>() {
          }
      ).getBody();
      List<OperationDto> finalOperationDtoList = new ArrayList<>();
      for (OperationDto operationDto : operationDtoList) {
        finalOperationDtoList.add((OperationDto) DataMask.mask(operationDto, crMUserRole,
            "operation", accessRepository, tokenCacheService).getBody());
      }

      return new ResponseEntity<>(finalOperationDtoList, HttpStatus.OK);
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
    List<ClientDto> clientDtoList = null;
    try {
      clientDtoList = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<ClientDto>>() {
          }
      ).getBody();
      List<ClientDto> finalClientDtoList = new ArrayList<>();
      for (ClientDto clientDto : clientDtoList) {
        finalClientDtoList.add((ClientDto) DataMask.mask(clientDto, crMUserRole,
            "client", accessRepository, tokenCacheService).getBody());
      }

      return new ResponseEntity<>(finalClientDtoList, HttpStatus.OK);
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

      return DataMask.mask(responseEntity.getBody(), crMUserRole, "clientLevel",
          accessRepository, tokenCacheService);
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
    List<ContactDto> contactDtoList = null;
    try {
      contactDtoList = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<ContactDto>>() {
          }
      ).getBody();
      List<ContactDto> finalContactDtoList = new ArrayList<>();
      for (ContactDto contactDto : contactDtoList) {
        finalContactDtoList.add((ContactDto) DataMask.mask(contactDto, crMUserRole,
            "contact", accessRepository, tokenCacheService).getBody());
      }

      return new ResponseEntity<>(finalContactDtoList, HttpStatus.OK);
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

      return DataMask.mask(responseEntity.getBody(), crMUserRole, "loanPayment",
          accessRepository, tokenCacheService);
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

      return DataMask.mask(responseEntity.getBody(), crMUserRole, "contact",
          accessRepository, tokenCacheService);
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

      return DataMask.mask(responseEntity.getBody(), crMUserRole, "client",
          accessRepository, tokenCacheService);
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }
}
