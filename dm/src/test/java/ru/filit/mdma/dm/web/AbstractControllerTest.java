package ru.filit.mdma.dm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.filit.mdma.dm.AbstractTest;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

  @Autowired
  protected MockMvc mockMvc;

  protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
    return mockMvc.perform(builder);
  }
}
