package ru.filit.mdma.dm.util;

import lombok.experimental.UtilityClass;
import ru.filit.oas.dm.model.Client;

@UtilityClass
public class EqualsUtil {

  public boolean isEqualsClient(Client clientForSearch, Client clientDto) {
    boolean isEquals = false;
    if (clientForSearch.getId() != null && clientDto.getId() != null) {
      isEquals = clientForSearch.getId().equals(clientDto.getId()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getLastname() != null && clientDto.getLastname() != null) {
      isEquals = clientForSearch.getLastname().equals(clientDto.getLastname()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getFirstname() != null && clientDto.getLastname() != null) {
      isEquals = clientForSearch.getFirstname().equals(clientDto.getFirstname()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPatronymic() != null && clientDto.getPatronymic() != null) {
      isEquals = clientForSearch.getPatronymic().equals(clientDto.getPatronymic()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getBirthDate() != null && clientDto.getBirthDate() != null) {
      isEquals = clientForSearch.getBirthDate().equals(clientDto.getBirthDate()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPassportSeries() != null && clientDto.getPassportSeries() != null) {
      isEquals =
          clientForSearch.getPassportSeries().equals(clientDto.getPassportSeries()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getPassportNumber() != null && clientDto.getPassportNumber() != null) {
      isEquals =
          clientForSearch.getPassportNumber().equals(clientDto.getPassportNumber()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }
    if (clientForSearch.getInn() != null && clientDto.getInn() != null) {
      isEquals = clientForSearch.getInn().equals(clientDto.getInn()) ? true : false;
      if (isEquals != true) {
        return isEquals;
      }
    }

    return isEquals;
  }
}
