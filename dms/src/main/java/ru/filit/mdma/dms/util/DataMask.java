package ru.filit.mdma.dms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.filit.mdma.dms.model.AccessRequestDto;
import ru.filit.mdma.dms.repository.AccessRepository;

/**
 * Класс для работы с маскировкой данных поступающих из приложения DM.
 */
@Slf4j
@UtilityClass
public class DataMask {

  private final String MANAGER_ROLE = "MANAGER";
  private final String SUPERVISOR_ROLE = "SUPERVISOR";
  private final String AUDITOR_ROLE = "AUDITOR";
  private final String ACCESS_VERSION = "2";
  private final ObjectWriter objectWriter
      = new ObjectMapper().writer().withDefaultPrettyPrinter();

  /**
   * Метод маскирования данных поступающих из приложения DM.
   */
  public <T> ResponseEntity mask(T body, String crMUserRole, String entityName,
      AccessRepository accessRepository) {
    log.info("Маскирование данных: {}", body);

    String jsonString = null;
    Map<String, Object> mapFromJson = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      jsonString = objectWriter.writeValueAsString(body);
      TypeReference ref = new TypeReference<Map<String, Object>>() {
      };
      mapFromJson = (Map<String, Object>) mapper.readValue(jsonString, ref);
    } catch (JsonProcessingException e) {
      log.error("cannot create Map from json " + e.getMessage());
      e.printStackTrace();
    }

    List<String> allowedAccessList =
        accessRepository.getAccessList(createAccessRequestDto(crMUserRole), entityName);
    mapFromJson = mapFromJson.entrySet().stream().map(e -> {
      if (!allowedAccessList.contains(e.getKey()) && !e.getKey().equals("deferment")) {
        e.setValue("****");
      }
      return e;
    }).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

    String jsonResult = "";
    try {
      jsonResult = mapper.writeValueAsString(mapFromJson);
    } catch (IOException e) {
      log.error("cannot create json from Map" + e.getMessage());
      return null;
    }
    Class bodyClass = body.getClass();
    body = null;
    try {
      body = (T) mapper.readValue(jsonResult, bodyClass);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<T>(body, HttpStatus.OK);
  }

  private AccessRequestDto createAccessRequestDto(String crMUserRole) {
    AccessRequestDto accessRequestDto = new AccessRequestDto();
    accessRequestDto.setVersion(ACCESS_VERSION);
    switch (crMUserRole) {
      case SUPERVISOR_ROLE:
        accessRequestDto.setRole(SUPERVISOR_ROLE);
        break;
      case AUDITOR_ROLE:
        accessRequestDto.setRole(AUDITOR_ROLE);
        break;
      default:
        accessRequestDto.setRole(MANAGER_ROLE);
    }
    return accessRequestDto;
  }
}
