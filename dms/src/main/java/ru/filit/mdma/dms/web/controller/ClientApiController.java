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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.filit.mdma.dms.audit.AuditService;
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
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public ClientApiController(AccessRepository accessRepository, RestTemplate restTemplate,
      TokenCacheService tokenCacheService, KafkaTemplate<String, String> kafkaTemplate) {
    this.accessRepository = accessRepository;
    this.restTemplate = restTemplate;
    this.tokenCacheService = tokenCacheService;
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Запрос списка Счетов клиента по его ID.
   */
  @PostMapping("/account")
  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Поиск Счетов клиента по входящим данным: {}", clientIdDto.getId());

    final String requestUrl = "http://localhost:8082/dm/client/account";
    clientIdDto = DataMask.demask(clientIdDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(clientIdDto, "account",
            accessRepository, tokenCacheService), requestId, crMUserName, ">/dm/client/account"));

    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    List<AccountDto> accountDtoList = createResponseList(requestUrl, requestEntity,
        new ParameterizedTypeReference<List<AccountDto>>() {
        });

    List<AccountDto> finalAccountDtoList = new ArrayList<>();
    List<String> finalAccountDtoListAudit = new ArrayList<>();
    for (AccountDto accountDto : accountDtoList) {
      finalAccountDtoList.add((AccountDto) DataMask.mask(accountDto, crMUserRole,
          "account", accessRepository, tokenCacheService).getBody());
      finalAccountDtoListAudit.add(DataMask.maskForAudit(accountDto, "account",
          accessRepository, tokenCacheService));
    }

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(finalAccountDtoListAudit.toString(), requestId, crMUserName,
            "</dm/client/account"));

    return new ResponseEntity<>(finalAccountDtoList, HttpStatus.OK);
  }

  /**
   * Запрос баланса по счету клиента.
   */
  @PostMapping("/account/balance")
  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск баланса клиента по входящим данным: {}", accountNumberDto);

    final String requestUrl = "http://localhost:8082/dm/client/account/balance";
    accountNumberDto = DataMask.demask(accountNumberDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(accountNumberDto, "dummy",
                accessRepository, tokenCacheService), requestId, crMUserName,
            ">/dm/client/account/balance"));

    HttpEntity requestEntity = new HttpEntity(accountNumberDto);
    ResponseEntity<CurrentBalanceDto> responseEntity = createResponseEntity(requestEntity,
        requestUrl, new ParameterizedTypeReference<CurrentBalanceDto>() {
        });

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(responseEntity.getBody(),
                "dummy", accessRepository, tokenCacheService),
            requestId, crMUserName, "</dm/client/account/balance"));

    return DataMask.mask(responseEntity.getBody(), crMUserRole, "dummy",
        accessRepository, tokenCacheService);
  }

  /**
   * Запрос Операций по счету клиента с ограничением количества вывода элементов.
   */
  @PostMapping("/account/operation")
  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto, String crMUserRole, String crMUserName) {
    log.info("Поиск Операций счета клиента по входящим данным: {}", operationSearchDto);

    final String requestUrl = "http://localhost:8082/dm/client/account/operation";
    operationSearchDto = DataMask.demask(operationSearchDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(operationSearchDto, "operation",
                accessRepository, tokenCacheService), requestId, crMUserName,
            ">/dm/client/account/operation"));

    HttpEntity requestEntity = new HttpEntity(operationSearchDto);
    List<OperationDto> operationDtoList = createResponseList(requestUrl, requestEntity,
        new ParameterizedTypeReference<List<OperationDto>>() {
        });

    List<OperationDto> finalOperationDtoList = new ArrayList<>();
    List<String> finalOperationDtoListAudit = new ArrayList<>();
    for (OperationDto operationDto : operationDtoList) {
      finalOperationDtoList.add((OperationDto) DataMask.mask(operationDto, crMUserRole,
          "operation", accessRepository, tokenCacheService).getBody());
      finalOperationDtoListAudit.add(DataMask.maskForAudit(operationDto, "operation",
          accessRepository, tokenCacheService));
    }

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(finalOperationDtoListAudit.toString(), requestId,
            crMUserName,
            "</dm/client/account/operation"));

    return new ResponseEntity<>(finalOperationDtoList, HttpStatus.OK);
  }

  /**
   * Запрос списка Клиентов по заданным параметрам.
   */
  @PostMapping
  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск клиентов по входящим данным: {}", clientSearchDto);

    final String requestUrl = "http://localhost:8082/dm/client/";
    clientSearchDto = DataMask.demask(clientSearchDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(clientSearchDto, "client",
            accessRepository, tokenCacheService), requestId, crMUserName, ">/dm/client"));

    HttpEntity requestEntity = new HttpEntity(clientSearchDto);
    List<ClientDto> clientDtoList = createResponseList(requestUrl, requestEntity,
        new ParameterizedTypeReference<List<ClientDto>>() {
        });

    List<ClientDto> finalClientDtoList = new ArrayList<>();
    List<String> finalClientDtoListAudit = new ArrayList<>();
    for (ClientDto clientDto : clientDtoList) {
      finalClientDtoList.add((ClientDto) DataMask.mask(clientDto, crMUserRole,
          "client", accessRepository, tokenCacheService).getBody());
      finalClientDtoListAudit.add(DataMask.maskForAudit(clientDto, "client",
          accessRepository, tokenCacheService));
    }

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(finalClientDtoListAudit.toString(), requestId, crMUserName,
            "</dm/client"));

    return new ResponseEntity<>(finalClientDtoList, HttpStatus.OK);
  }

  /**
   * Получение уровня клиента.
   */
  @PostMapping("/level")
  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Получение уровня клиента по входящим данным: {}", clientIdDto);

    final String requestUrl = "http://localhost:8082/dm/client/level";
    clientIdDto = DataMask.demask(clientIdDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(clientIdDto, "clientLevel",
            accessRepository, tokenCacheService), requestId, crMUserName, ">/dm/client/level"));

    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<ClientLevelDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<ClientLevelDto>() {
        });

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(responseEntity.getBody(),
                "clientLevel", accessRepository, tokenCacheService),
            requestId, crMUserName, "</dm/client/level"));

    return DataMask.mask(responseEntity.getBody(), crMUserRole, "clientLevel",
        accessRepository, tokenCacheService);
  }

  /**
   * Запрос списка Контактов клиента по его ID.
   */
  @PostMapping("/contact")
  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Поиск Контактов клиента по входящим данным: {}", clientIdDto.getId());

    final String requestUrl = "http://localhost:8082/dm/client/contact";
    clientIdDto = DataMask.demask(clientIdDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(clientIdDto, "contact",
            accessRepository, tokenCacheService), requestId, crMUserName, ">/dm/client/contact"));

    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    List<ContactDto> contactDtoList = createResponseList(requestUrl, requestEntity,
        new ParameterizedTypeReference<List<ContactDto>>() {
        });

    List<ContactDto> finalContactDtoList = new ArrayList<>();
    List<String> finalContactDtoListAudit = new ArrayList<>();
    for (ContactDto contactDto : contactDtoList) {
      finalContactDtoList.add((ContactDto) DataMask.mask(contactDto, crMUserRole,
          "contact", accessRepository, tokenCacheService).getBody());
      finalContactDtoListAudit.add(DataMask.maskForAudit(contactDto, "contact",
          accessRepository, tokenCacheService));
    }

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(finalContactDtoListAudit.toString(), requestId, crMUserName,
            "</dm/client/contact"));

    return new ResponseEntity<>(finalContactDtoList, HttpStatus.OK);
  }

  /**
   * Получение суммы процентных платежей по счету Овердрафт.
   */
  @PostMapping("/account/loan-payment")
  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    log.info("Поиск суммы процентных платежей по счету Овердрафт по входящим данным: {}",
        accountNumberDto);

    final String requestUrl = "http://localhost:8082/dm/client/account/loan-payment";
    accountNumberDto = DataMask.demask(accountNumberDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(accountNumberDto, "loanPayment",
                accessRepository, tokenCacheService), requestId, crMUserName,
            ">/dm/client/account/loan-payment"));

    HttpEntity requestEntity = new HttpEntity(accountNumberDto);
    ResponseEntity<LoanPaymentDto> responseEntity = createResponseEntity(requestEntity,
        requestUrl, new ParameterizedTypeReference<LoanPaymentDto>() {
        });

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(responseEntity.getBody(),
                "loanPayment", accessRepository, tokenCacheService),
            requestId, crMUserName, "</dm/client/account/loan-payment"));

    return DataMask.mask(responseEntity.getBody(), crMUserRole, "loanPayment",
        accessRepository, tokenCacheService);
  }

  /**
   * Сохранение контакта клиента.
   */
  @PostMapping("/contact/save")
  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto, String crMUserRole,
      String crMUserName) {
    log.info("Сохранение контакта клиента по входящим данным: {}", contactDto);

    final String requestUrl = "http://localhost:8082/dm/client/contact/save";
    contactDto = DataMask.demask(contactDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(contactDto, "contact",
                accessRepository, tokenCacheService), requestId, crMUserName,
            ">/dm/client/contact/save"));

    HttpEntity requestEntity = new HttpEntity(contactDto);
    ResponseEntity<ContactDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<ContactDto>() {
        });

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(responseEntity.getBody(),
                "contact", accessRepository, tokenCacheService),
            requestId, crMUserName, "</dm/client/contact/save"));

    return DataMask.mask(responseEntity.getBody(), crMUserRole, "contact",
        accessRepository, tokenCacheService);
  }

  /**
   * Запрос информации о Клиенте по его ID.
   */
  @PostMapping("/info")
  @Override
  public ResponseEntity<ClientDto> getClientInfo(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    log.info("Запрос информации о Клиенте по его ID: {}", clientIdDto);

    final String requestUrl = "http://localhost:8082/dm/client/info";
    clientIdDto = DataMask.demask(clientIdDto, tokenCacheService);

    String requestId = AuditService.createRequestId();
    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(clientIdDto, "client",
            accessRepository, tokenCacheService), requestId, crMUserName, ">/dm/client/info"));

    HttpEntity requestEntity = new HttpEntity(clientIdDto);
    ResponseEntity<ClientDto> responseEntity = createResponseEntity(requestEntity, requestUrl,
        new ParameterizedTypeReference<ClientDto>() {
        });

    kafkaTemplate.send("dm-audit",
        AuditService.createAuditRecord(DataMask.maskForAudit(responseEntity.getBody(),
                "client", accessRepository, tokenCacheService),
            requestId, crMUserName, "</dm/client/info"));

    return DataMask.mask(responseEntity.getBody(), crMUserRole, "client",
        accessRepository, tokenCacheService);
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

  private <T> List createResponseList(String requestUrl, HttpEntity requestEntity,
      ParameterizedTypeReference<List<T>> responseType) {
    URI uri = null;
    try {
      uri = new URI(requestUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    List<T> listDto = null;
    try {
      listDto = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType).getBody();

      return listDto;
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
  }
}