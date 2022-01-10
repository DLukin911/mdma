package ru.filit.mdma.dms.util.dto;

import static ru.filit.mdma.dms.util.DataMask.*;

import lombok.experimental.UtilityClass;
import ru.filit.mdma.dms.model.AccessRequestDto;

/**
 * Класс для создания транспортного объекта AccessRequestDto.
 */
@UtilityClass
public class CreateAccessDto {

  public AccessRequestDto createAccessRequestDto(String crMUserRole, String accessVersion) {
    AccessRequestDto accessRequestDto = new AccessRequestDto();
    accessRequestDto.setVersion(accessVersion);
    switch (crMUserRole) {
      case "[SUPERVISOR]":
        accessRequestDto.setRole(SUPERVISOR_ROLE);
        break;
      case "AUDITOR":
        accessRequestDto.setRole(AUDITOR_ROLE);
        break;
      default:
        accessRequestDto.setRole(MANAGER_ROLE);
    }

    return accessRequestDto;
  }
}