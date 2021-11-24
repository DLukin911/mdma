package ru.filit.mdma.dm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.dm.JsonUtil;
import ru.filit.oas.dm.model.Client;

@Repository
public class EntityRepository {
  ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  File file = new File(classLoader.getResource("database/entity-repository.yaml").getFile());

  // Instantiating a new ObjectMapper as a YAMLFactory
  static ObjectMapper om = new ObjectMapper(new YAMLFactory());


  public List<Client> getClient() {
    List<Client> clientList = null;
    om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    {
      try {
        clientList = om.readValue(file, new TypeReference<List<Client>>(){});
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return clientList;
  }

  public void setClient() {

    Client client= new Client();
    client.setId("111");
    client.lastname("232323");
    client.setFirstname("f5435435435");
    client.setPatronymic("fgfgfgf");
    client.setBirthDate(19000321L);
    client.setPassportSeries("bnbnbn");
    client.setPassportNumber("777777777");
    client.setInn("7777777777");
    client.setAddress("7777777777777");

    try {
      om.writeValue(new File("src/main/resources/database/person2.yaml"), client);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
