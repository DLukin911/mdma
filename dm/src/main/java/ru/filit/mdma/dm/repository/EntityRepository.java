package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.oas.dm.model.Client;

/**
 * Класс для работы с базами данных на основе YAML файлов.
 */
@Slf4j
@Repository
public class EntityRepository {

  /**
   * Получение списка сущностей Клиента.
   */
  @Cacheable
  public List<Client> getClientList() {
    log.info("Запрос списка всех клиентов из базы данных");
    List<Client> clientList = null;
    try {
      clientList = FileUtil.getMapper()
          .readValue(FileUtil.getEntityFile(), new TypeReference<List<Client>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return clientList;
  }
}
