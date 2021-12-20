package ru.filit.mdma.dms.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.filit.mdma.dms.model.AccessDto;
import ru.filit.mdma.dms.model.AccessRequestDto;
import ru.filit.mdma.dms.util.exception.NotFoundException;

/**
 * Репозиторий сущностей Access приложения DMS.
 */
@Slf4j
@Repository
public class AccessRepository {

  private final RestTemplate restTemplate;

  @Autowired
  public AccessRepository(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Получение полей не подлежащих маскированию из приложения DM.
   */
  public List<String> getAccessList(AccessRequestDto accessRequestDto, String entityName) {
    log.info("Получение информации о разрешениях доступа к данным");

    final String clientSaveContactUrl = "http://localhost:8082/dm/access";
    URI uri = null;
    try {
      uri = new URI(clientSaveContactUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HttpEntity requestEntity = new HttpEntity(accessRequestDto);
    List<AccessDto> accessList = null;
    try {
      accessList = restTemplate.exchange(uri, HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<List<AccessDto>>() {
          }
      ).getBody();
    } catch (Exception e) {
      throw new NotFoundException("По данному запросу информация не найдена.");
    }

    String finalEntityName = entityName;
    return accessList.stream()
        .filter(accessDto -> accessDto.getEntity().equals(finalEntityName))
        .map(AccessDto::getProperty).collect(Collectors.toList());
  }
}
