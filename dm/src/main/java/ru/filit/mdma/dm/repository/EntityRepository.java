package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.oas.dm.model.Client;

/**
 * Класс для работы с базами данных на основе YAML файлов.
 */
@Slf4j
@Repository
public class EntityRepository {

  private static Map<String, Client> clientCache = new HashMap<>();

  static {
    enableClientCache();
  }

  /**
   * Получение списка сущностей Клиента.
   */
  public List<Client> getClientList() {
    log.info("Запрос списка всех клиентов из кеша");
    return clientCache.values().stream().toList();
  }

  /**
   * Получение сущности Клиента по Id.
   */
  public Client getClientById(String id) {
    log.info("Запрос сущности Клиент по id: {}", id);
    return clientCache.get(id);
  }

  /**
   * Активация кеша сущности Клиент (id, Клиент).
   */
  private static void enableClientCache() {
    log.info("Запрос списка всех клиентов из базы данных");
    List<Client> clientList = null;
    try {
      clientList = FileUtil.getMapper()
          .readValue(FileUtil.getEntityFile(), new TypeReference<List<Client>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }

    clientCache = clientList.stream().collect(Collectors.toMap(Client::getId, client -> client));
  }
}
