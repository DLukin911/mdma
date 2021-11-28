package ru.filit.mdma.dm.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.filit.mdma.dm.web.controller.ClientApiController;


class EntityControllerTest extends AbstractControllerTest {

  private static final String REST_URL = ClientApiController.REST_URL + '/';

/*  @Test
  void get() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andDo(print())
        // https://jira.spring.io/browse/SPR-14472
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(USER_MATCHER.contentJson(admin));
  }*/

  @Test
  void getNotFound() throws Exception {
    perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}