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
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;

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
    Client clientForSearch = MapperUtil.INSTANCE.clientForSearch(clientSearchDto);
    List<Client> clientList = entityRepository.getClientList().stream()
        .filter(client -> EqualsUtil.isEqualsClient(clientForSearch, client))
        .collect(Collectors.toList());
    if(clientList.isEmpty()) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }
    List<ClientDto> clientDtoList = new ArrayList<>();
    for (Client client : clientList) {
      clientDtoList.add(MapperUtil.INSTANCE.convert(client));
    }

    return clientDtoList;
  }
}