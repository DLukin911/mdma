package ru.filit.mdma.dm.repository;

import static ru.filit.mdma.dm.util.FileUtil.getEntityFile;
import static ru.filit.mdma.dm.util.FileUtil.getMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;
import ru.filit.oas.dm.model.Client;

@Repository
@EnableCaching
public class EntityRepository {

  @Cacheable("clientList")
  public List<Client> getClientList() {
    List<Client> clientList = null;
    try {
      clientList = getMapper().readValue(getEntityFile(), new TypeReference<List<Client>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return clientList;
  }

  @CacheEvict(value = "clientList", allEntries = true)
  public List<Client> saveClientList(List<Client> clientList) {
    try {
      getMapper().writeValue(getEntityFile(), clientList);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return clientList;
  }
}
