package ru.filit.mdma.dm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.filit.mdma.dm.repository.EntityRepository;
import ru.filit.mdma.dm.util.EqualsUtil;
import ru.filit.mdma.dm.util.MapperUtil;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;

@Service
public class EntityService {

  private EntityRepository entityRepository = new EntityRepository();

  public List<ClientDto> getClient(ClientSearchDto clientSearchDto) {
    Client clientForSearch = MapperUtil.INSTANCE.clientForSearch(clientSearchDto);
    List<Client> clientList = entityRepository.getClientList().stream()
        .filter(client -> EqualsUtil.isEqualsClient(clientForSearch, client))
        .collect(Collectors.toList());
    List<ClientDto> clientDtoList = new ArrayList<>();
    for (Client client : clientList) {
      clientDtoList.add(MapperUtil.INSTANCE.convert(client));
    }

    return clientDtoList;
  }
}