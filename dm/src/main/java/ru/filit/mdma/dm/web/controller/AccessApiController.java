package ru.filit.mdma.dm.web.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.filit.oas.dm.web.controller.AccessApi;
import ru.filit.oas.dm.web.dto.AccessDto;
import ru.filit.oas.dm.web.dto.AccessRequestDto;

@Controller
public class AccessApiController implements AccessApi {

  /**
   * POST /access : Запрос прав доступа для Роли
   *
   * @param accessRequestDto (required)
   * @return Права досиупа определены (status code 200)
   */
  @Override
  public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto accessRequestDto) {
    return null;
  }
}
