package ru.filit.mdma.dm.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonAccount1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonBalanceAmount;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonClient1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonClientGetById;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonContact1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonOperationList;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonUpdateContactSave;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.filit.mdma.dm.util.FileUtil;
import ru.filit.mdma.dm.web.controller.ClientApiController;
import ru.filit.oas.dm.model.Contact;

class EntityControllerTest extends AbstractControllerTest {

  private static final String REST_URL = ClientApiController.REST_URL;

  private List<Contact> contactCache;

  {
    try {
      contactCache = FileUtil.getMapper()
          .readValue(FileUtil.getContactsFile(), new TypeReference<List<Contact>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getClient() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL).content("{\n"
        + "\"lastname\": \"Ворошилов\",\n"
        + "\"passport\": \"6215 352617\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonClient1));
  }

  @Test
  void getClientNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getContact() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/contact").content("{\n"
        + "\"id\": \"80302\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonContact1));
  }

  @Test
  void getContactNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/contact")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAccount() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account").content("{\n"
        + "\"id\": \"74914\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonAccount1));
  }

  @Test
  void getAccountNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getOperation() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account/operation").content("{\n"
        + "\"accountNumber\": \"40817810670114037905\",\n"
        + "\"quantity\": \"3\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonOperationList));
  }

  @Test
  void getOperationNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account/operation")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getBalance() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account/balance").content("{\n"
        + "\"accountNumber\": \"40817810670114037905\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonBalanceAmount));
  }

  @Test
  void getBalanceNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/account/balance")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void saveNewContact() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/contact/save").content("{\n"
        + "\"clientId\": \"95471\",\n"
        + "\"type\": \"PHONE\",\n"
        + "\"value\": \"+79805243600\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8));
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DirtiesContext
  void updateContact() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/contact/save").content("{\n"
        + "\"id\": \"38523\",\n"
        + "\"clientId\": \"95471\",\n"
        + "\"type\": \"PHONE\",\n"
        + "\"value\": \"+79805243600\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonUpdateContactSave));
    try {
      FileUtil.getMapper().writeValue(FileUtil.getContactsFile(), contactCache);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void saveContactNotFoundClient() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/contact/save")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getClientById() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/info").content("{\n"
        + "\"id\": \"95471\"\n"
        + "}").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().encoding(StandardCharsets.UTF_8))
        .andExpect(content().string(jsonClientGetById));
  }

  @Test
  void getClientByIdNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL + "/info").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}