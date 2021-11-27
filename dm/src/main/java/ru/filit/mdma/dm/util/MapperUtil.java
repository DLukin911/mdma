package ru.filit.mdma.dm.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;

/**
 * Описание.
 */
@Mapper
public abstract class MapperUtil {

  public static MapperUtil INSTANCE = Mappers.getMapper(MapperUtil.class);

  //******** Converting methods from Client Entity to DTO*******

  /**
   * Описание.
   */
  @Mappings({
      @Mapping(target = "birthDate", expression = "java(changeBirthDateToLong(clientSearchDto.getBirthDate()))"),
      @Mapping(target = "passportSeries", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 0))"),
      @Mapping(target = "passportNumber", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 1))")
  })
  public abstract Client clientForSearch(ClientSearchDto clientSearchDto);

  //******** Converting methods from DTO to Client Entity*******

  /**
   * Описание.
   */
  @Mapping(target = "birthDate", expression = "java(changeBirthDateToString(client.getBirthDate()))")
  public abstract ClientDto convert(Client client);

  protected String getPassportSplitNumber(String passport, int num) {
    if (passport == null) {
      return null;
    }
    return passport.split(" ")[num];
  }

  protected Long changeBirthDateToLong(String birthDate) {
    if (birthDate == null) {
      return null;
    }
    return Long.parseLong(birthDate.replaceAll("[^0-9]", ""));
  }

  protected String changeBirthDateToString(Long birthDate) {
    if (birthDate == null) {
      return null;
    }
    return birthDate.toString()
        .replaceAll("(.{4})(?!$)", "$1-")
        .replaceAll("(.{7})(?!$)", "$1-");
  }
}