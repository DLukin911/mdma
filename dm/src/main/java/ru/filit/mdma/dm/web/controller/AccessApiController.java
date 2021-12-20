package ru.filit.mdma.dm.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.dm.service.EntityService;
import ru.filit.oas.dm.web.controller.AccessApi;
import ru.filit.oas.dm.web.dto.AccessDto;
import ru.filit.oas.dm.web.dto.AccessRequestDto;

/**
 * Реализация API интерфейса доступы Роли пользователя.
 */
@Slf4j
@RestController
@RequestMapping(value = AccessApiController.REST_URL, produces = "application/json; charset=UTF-8",
    consumes = "application/json; charset=UTF-8")
public class AccessApiController implements AccessApi {

  public static final String REST_URL = "/access";

  private final EntityService entityService;

  public AccessApiController(EntityService entityService) {
    this.entityService = entityService;
  }

  /**
   * Запрос прав доступа для Роли.
   */
  @PostMapping
  @Override
  public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto accessRequestDto) {
    log.info("Запрос прав доступа по заданой Роли пользователя: {}", accessRequestDto);

    List<AccessDto> accessDtoList = entityService.getAccess(accessRequestDto);
    if (accessDtoList.isEmpty()) {
      log.info("Права доступа по Роли не найдены.");
      return ResponseEntity.badRequest().body(accessDtoList);
    }
    log.info("Права доступа по Роли успешно найдены {}", accessDtoList);

    return new ResponseEntity<>(accessDtoList, HttpStatus.OK);
  }
}
