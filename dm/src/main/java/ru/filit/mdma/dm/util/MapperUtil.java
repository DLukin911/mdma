package ru.filit.mdma.dm.util;

import static ru.filit.oas.dm.model.Contact.TypeEnum.EMAIL;
import static ru.filit.oas.dm.model.Contact.TypeEnum.PHONE;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import ru.filit.oas.dm.model.Operation;
import ru.filit.oas.dm.web.dto.AccountDto;
import ru.filit.oas.dm.web.dto.ClientDto;
import ru.filit.oas.dm.web.dto.ClientSearchDto;
import ru.filit.oas.dm.web.dto.ContactDto;
import ru.filit.oas.dm.web.dto.OperationDto;

/**
 * Класс для маппинга сущностей приложения DM.
 */
@Mapper
public abstract class MapperUtil {

  public static MapperUtil INSTANCE = Mappers.getMapper(MapperUtil.class);

  //******** Converting methods from Entity to DTO *******

  /**
   * Преобразование транспротного обьекта для поиска Клиента в сущность Клиент.
   */
  @Mappings({
      @Mapping(target = "birthDate", expression = "java(changeDateToLong(clientSearchDto.getBirthDate()))"),
      @Mapping(target = "passportSeries", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 0))"),
      @Mapping(target = "passportNumber", expression = "java(getPassportSplitNumber(clientSearchDto.getPassport(), 1))")
  })
  public abstract Client clientForSearch(ClientSearchDto clientSearchDto);

  //******** Converting methods from DTO to Entity *******

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
   * Преобразование сущности Операции клиента в транспортный обьект.
   */
  @Mappings({
      @Mapping(target = "operDate",
          expression = "java(changeDateToStringWithT(operation.getOperDate(), 0))"),
      @Mapping(target = "amount", expression = "java(amountWithTwoZero(operation.getAmount()))")
  })
  public abstract OperationDto convert(Operation operation);

  //******** Utility methods *******

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
   * Преобразуем дату в Long для сохранения в БД.
   */
  protected Long changeDateToLong(String dateString) {
    if (dateString == null) {
      return null;
    }
    DateTimeFormatter dateFormatter
        = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    long secondsSinceEpoch = LocalDate.parse(dateString, dateFormatter)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli() / 1000;

    return secondsSinceEpoch;
  }

  /**
   * Преобразуем дату рождения из Long в String, получения формата YYYY-MM-DD.
   */
  protected String changeDateToString(Long dateLong) {
    if (dateLong == null) {
      return null;
    }
    dateLong *= 1000L;
    Instant instant = new Date(dateLong).toInstant();
    LocalDateTime ldt = instant.atOffset(ZoneOffset.ofHours(3)).toLocalDateTime();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    return ldt.format(fmt);
  }

  /**
   * Преобразуем дату из Long в String, получения формата YYYY-MM-DD'T'HH:MM:SS.
   */
  protected String changeDateToStringWithT(Long dateLong, int dummy) {
    if (dateLong == null) {
      return null;
    }
    dateLong *= 1000;
    Instant instant = new Date(dateLong).toInstant();
    LocalDateTime ldt = instant.atOffset(ZoneOffset.ofHours(3)).toLocalDateTime();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

  /**
   * Преобразуем amount к двум цифрам после точки.
   */
  protected String amountWithTwoZero(BigDecimal amount) {
    return new DecimalFormat("0.00").format(amount).replace(",", ".");
  }
}