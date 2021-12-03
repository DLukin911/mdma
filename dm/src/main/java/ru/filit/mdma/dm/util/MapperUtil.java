package ru.filit.mdma.dm.util;

import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;
import static ru.filit.oas.dm.model.Contact.TypeEnum.PHONE;

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
import ru.filit.oas.dm.model.Account;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.ContactDto;

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
      @Mapping(target = "birthDate", expression = "java(changeDateToLong(clientSearchDto.getBirthDate()))"),
      @Mapping(target = "passportSeries", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 0))"),
      @Mapping(target = "passportNumber", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 1))")
  })
  public abstract Client clientForSearch(ClientSearchDto clientSearchDto);

  //******** Converting methods from DTO to Entity*******

  /**
   * Преобразование сущности Клиента в транспортный обьект.
   */
  @Mapping(target = "birthDate", expression = "java(changeDateToString(client.getBirthDate()))")
  public abstract ClientDto convert(Client client);

  /**
   * Преобразование сущности Контакт клиента в транспортный обьект.
   */
  @Mapping(target = "shortcut", expression = "java(shortcutContact(contact))")
  public abstract ContactDto convert(Contact contact);

  /**
   * Преобразование сущности Счета клиента в транспортный обьект.
   */
  @Mapping(target = "shortcut", expression = "java(shortcutAccount(account))")
  public abstract AccountDto convert(Account account);

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
  protected Long changeDateToLong(String dateString) {
    if (dateString == null) {
      return null;
    }
    DateTimeFormatter dateFormatter
        = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    long millisecondsSinceEpoch = LocalDate.parse(dateString, dateFormatter)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli();

    return millisecondsSinceEpoch;
  }

  /**
   * Преобразуем дату рождения в String, получения формата YYYY-MM-DD.
   */
  protected String changeDateToString(Long dateLong) {
    if (dateLong == null) {
      return null;
    }
    Instant instant = new Date(dateLong).toInstant();
    LocalDateTime ldt = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    return ldt.format(fmt);
  }

  /**
   * Создаем атрибут shortcut для сущности Контакт клиента.
   */
  protected String shortcutContact(Contact contact) {
    if (contact.getType().equals(PHONE)) {
      return contact.getValue().substring(Math.max(0, contact.getValue().length() - 4));
    } else if (contact.getType().equals(EMAIL)) {
      int index = contact.getValue().indexOf('@');
      return contact.getValue().substring(index - 1);
    } else {
      return null;
    }
  }

  /**
   * Создаем атрибут shortcut для сущности Счет клиента.
   */
  protected String shortcutAccount(Account account) {
    if (account.getNumber() != null) {
      return account.getNumber().substring(Math.max(0, account.getNumber().length() - 4));
    } else {
      return null;
    }
  }
}