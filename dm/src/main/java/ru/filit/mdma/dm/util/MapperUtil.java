package ru.filit.mdma.dm.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;

/**
 * Класс для маппинга сущностей приложения DM.
 */
@Mapper
public abstract class MapperUtil {

  public static MapperUtil INSTANCE = Mappers.getMapper(MapperUtil.class);

  //******** Converting methods from Entity to DTO*******

  /**
   * Преобразование транспротного обьекта для поиска Клиента в сущность Клиент.
   */
  @Mappings({
      @Mapping(target = "birthDate", expression = "java(changeBirthDateToLong(clientSearchDto.getBirthDate()))"),
      @Mapping(target = "passportSeries", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 0))"),
      @Mapping(target = "passportNumber", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 1))")
  })
  public abstract Client clientForSearch(ClientSearchDto clientSearchDto);

  //******** Converting methods from DTO to Entity*******

  /**
   * Преобразование сущности Клиента в транспортный обьект.
   */
  @Mapping(target = "birthDate", expression = "java(changeBirthDateToString(client.getBirthDate()))")
  public abstract ClientDto convert(Client client);

  /**
   * Разделение паспортных данных на серию и номер.
   */
  protected String getPassportSplitNumber(String passport, int num) {
    if (passport == null) {
      return null;
    }

    return passport.split(" ")[num];
  }

  /**
   * Преобразуем дату рождения в Long для сохранения в БД.
   */
  protected Long changeBirthDateToLong(String birthDate) {
    if (birthDate == null) {
      return null;
    }
    DateTimeFormatter dateFormatter
        = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    long millisecondsSinceEpoch = LocalDate.parse(birthDate, dateFormatter)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli();

    return millisecondsSinceEpoch;
  }

  /**
   * Преобразуем дату рождения в String, получения формата YYYY-MM-DD.
   */
  protected String changeBirthDateToString(Long birthDate) {
    if (birthDate == null) {
      return null;
    }
    Instant instant = new Date(birthDate).toInstant();
    LocalDateTime ldt = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    return ldt.format(fmt);
  }
}