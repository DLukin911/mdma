package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.oas.dm.model.Client;

@Repository
@EnableCaching
public class EntityRepository {

  @Cacheable("clientList")
  public List<Client> getClientList() {
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
