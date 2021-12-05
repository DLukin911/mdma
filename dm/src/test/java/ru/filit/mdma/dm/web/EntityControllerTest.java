package ru.filit.mdma.dm.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonAccount1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonClient1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonContact1;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonOperationList;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.filit.mdma.dm.web.controller.ClientApiController;

class EntityControllerTest extends AbstractControllerTest {

  private static final String REST_URL = ClientApiController.REST_URL;

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
        + "\"id\": \"95471\"\n"
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
        + "\"id\": \"95471\"\n"
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
        + "\"accountNumber\": \"40817810853110005823\",\n"
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
}