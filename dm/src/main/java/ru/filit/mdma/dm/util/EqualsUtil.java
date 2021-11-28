package ru.filit.mdma.dm.util;

import lombok.experimental.UtilityClass;
import ru.filit.oas.dm.model.Client;

/**
 * Класс для сравнения полей сущностей.
 */
@UtilityClass
public class EqualsUtil {

  /**
   * Сравнение полей Клиента для поиска и обьекта Клиент.
   */
  public boolean isEqualsClient(Client clientForSearch, Client client) {
    boolean isEquals = false;
    if (clientForSearch.getId() != null && client.getId() != null) {
      isEquals = clientForSearch.getId().equals(client.getId()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getLastname() != null && client.getLastname() != null) {
      isEquals = clientForSearch.getLastname().equals(client.getLastname()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getFirstname() != null && client.getLastname() != null) {
      isEquals = clientForSearch.getFirstname().equals(client.getFirstname()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPatronymic() != null && client.getPatronymic() != null) {
      isEquals = clientForSearch.getPatronymic().equals(client.getPatronymic()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getBirthDate() != null && client.getBirthDate() != null) {
      isEquals = clientForSearch.getBirthDate().equals(client.getBirthDate()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPassportSeries() != null && client.getPassportSeries() != null) {
      isEquals =
          clientForSearch.getPassportSeries().equals(client.getPassportSeries()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPassportNumber() != null && client.getPassportNumber() != null) {
      isEquals =
          clientForSearch.getPassportNumber().equals(client.getPassportNumber()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getInn() != null && client.getInn() != null) {
      isEquals = clientForSearch.getInn().equals(client.getInn()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }

    return isEquals;
  }
}
