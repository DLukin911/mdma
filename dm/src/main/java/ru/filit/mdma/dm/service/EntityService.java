package ru.filit.mdma.dm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.repository.EntityRepository;
import ru.filit.mdma.dm.util.EqualsUtil;
import ru.filit.mdma.dm.util.MapperUtil;
import ru.filit.mdma.dm.util.exception.NotFoundException;
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientIdDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.ContactDto;

/**
 * Класс для сервисных операций с сущностями.
 */
@Slf4j
@Service
public class EntityService {

  private final EntityRepository entityRepository;

  public EntityService(EntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }

  /**
   * Запрос списка сущностей Клиент через транспортный объект поиска Клиента.
   */
  public List<ClientDto> getClient(ClientSearchDto clientSearchDto) {
    log.info("Запрос списка клиентов из Entity Repository, параметры запроса: {}", clientSearchDto);

    if (clientSearchDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    Client clientForSearch = MapperUtil.INSTANCE.clientForSearch(clientSearchDto);
    List<Client> clientList = entityRepository.getClientList().stream()
        .filter(client -> EqualsUtil.isEqualsClient(clientForSearch, client))
        .collect(Collectors.toList());
    if (clientList.isEmpty()) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<ClientDto> clientDtoList = new ArrayList<>();
    for (Client client : clientList) {
      clientDtoList.add(MapperUtil.INSTANCE.convert(client));
    }

    return clientDtoList;
  }

  /**
   * Запрос списка сущностей Контакты клиента по его ID.
   */
  public List<ContactDto> getContact(ClientIdDto clientIdDto) {
    log.info("Запрос списка Контактов клиентов из Entity Repository, параметры запроса: {}",
        clientIdDto);

    if (clientIdDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<Contact> contactList = entityRepository.getContactByClientId(clientIdDto.getId());
    if (contactList.isEmpty()) {
      throw new NotFoundException("По данному ID контакты не найдены.");
    }
    List<ContactDto> contactDtoList = new ArrayList<>();
    for (Contact contact : contactList) {
      contactDtoList.add(MapperUtil.INSTANCE.convert(contact));
    }

    return contactDtoList;
  }

  /**
   * Запрос списка сущностей Счетов клиента по его ID.
   */
  public List<AccountDto> getAccount(ClientIdDto clientIdDto) {
    log.info("Запрос списка Счетов клиентов из Entity Repository, параметры запроса: {}",
        clientIdDto);

    if (clientIdDto == null) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<Account> accountList = entityRepository.getAccountByClientId(clientIdDto.getId());
    if (accountList.isEmpty()) {
      throw new NotFoundException("По данному ID счета не найдены.");
    }
    List<AccountDto> accountDtoList = new ArrayList<>();
    for (Account account : accountList) {
      accountDtoList.add(MapperUtil.INSTANCE.convert(account));
    }

    return accountDtoList;
  }
}