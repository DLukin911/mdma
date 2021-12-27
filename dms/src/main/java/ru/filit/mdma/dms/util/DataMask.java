package ru.filit.mdma.dms.util;

import static ru.filit.mdma.dms.util.dto.CreateAccessDto.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.filit.mdma.dms.repository.AccessRepository;
import ru.filit.mdma.dms.service.TokenCacheService;

/**
 * Класс для работы с маскировкой данных поступающих из приложения DM.
 */
@Slf4j
@UtilityClass
public class DataMask {

  public final String ACCESS_VERSION = "3";
  public final String MANAGER_ROLE = "MANAGER";
  public final String SUPERVISOR_ROLE = "SUPERVISOR";
  public final String AUDITOR_ROLE = "AUDITOR";
  private final ObjectWriter objectWriter
      = new ObjectMapper().writer().withDefaultPrettyPrinter();

  /**
   * Метод маскирования данных поступающих из приложения DM.
   */
  public <T> ResponseEntity mask(T body, String crMUserRole, String entityName,
      AccessRepository accessRepository, TokenCacheService tokenCacheService) {
    log.info("Маскирование данных: {}", body);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> mapFromJson = createJsonFromObject(mapper, body);

    List<String> allowedAccessList =
        accessRepository.getAccessList(createAccessRequestDto(crMUserRole, ACCESS_VERSION),
            entityName);
    mapFromJson = mapFromJson.entrySet().stream().map(e -> {
      if (!allowedAccessList.contains(e.getKey()) && !e.getKey().equals("deferment")) {
        String s = tokenCacheService.saveTokenByValue(e.getValue().toString());
        e.setValue(s);
      }
      return e;
    }).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

    return new ResponseEntity<T>(createObjectFromJson(mapFromJson, mapper, body), HttpStatus.OK);
  }

  /**
   * Метод демаскирования данных поступающих из приложения CRM-BH.
   */
  public <T> T demask(T body, TokenCacheService tokenCacheService) {
    log.info("Демаскирование данных, если применимо: {}", body);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> mapFromJson = createJsonFromObject(mapper, body);

    Pattern pattern = Pattern.compile("#(.*?)#");
    mapFromJson = mapFromJson.entrySet().stream().map(e -> {
      if (e.getValue() != null && pattern.matcher(e.getValue().toString()).matches()) {
        String s = tokenCacheService.getValueByToken(e.getValue().toString());
        e.setValue(s);
      }
      return e;
    }).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

    return createObjectFromJson(mapFromJson, mapper, body);
  }

  private <T> T createObjectFromJson(Map<String, Object> mapFromJson, ObjectMapper mapper, T body) {
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

    return body;
  }

  private Map<String, Object> createJsonFromObject(ObjectMapper mapper, Object body) {
    String jsonString = null;
    Map<String, Object> mapFromJson = null;
    try {
      jsonString = objectWriter.writeValueAsString(body);
      TypeReference ref = new TypeReference<Map<String, Object>>() {
      };
      mapFromJson = (Map<String, Object>) mapper.readValue(jsonString, ref);
    } catch (JsonProcessingException e) {
      log.error("cannot create Map from json " + e.getMessage());
      e.printStackTrace();
    }

    return mapFromJson;
  }
}
