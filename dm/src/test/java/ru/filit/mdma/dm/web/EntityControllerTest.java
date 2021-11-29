package ru.filit.mdma.dm.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.filit.mdma.dm.testdata.EntityWebTestData.jsonClient1;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.filit.mdma.dm.web.controller.ClientApiController;

class EntityControllerTest extends AbstractControllerTest {

  private static final String REST_URL = ClientApiController.REST_URL + '/';

  @Test
  void get() throws Exception {
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
  void getNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}