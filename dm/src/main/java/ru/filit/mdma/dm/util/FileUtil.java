package ru.filit.mdma.dm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import lombok.experimental.UtilityClass;

/**
 * Класс для работы с файлами ДБ на основе yml.
 */
@UtilityClass
public class FileUtil {

  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  private final PropertiesReaderUtil propsReader = new PropertiesReaderUtil("storage.properties");

  private final String CLIENTS_FILE = propsReader.get("clients.path");
  private final String CONTACTS_FILE = propsReader.get("contacts.path");
  private final String ACCOUNTS_FILE = propsReader.get("accounts.path");
  private final File clientsFile = new File(CLIENTS_FILE);
  private final File contactsFile = new File(CONTACTS_FILE);
  private final File accountsFile = new File(ACCOUNTS_FILE);

  public File getClientsFile() {
    return clientsFile;
  }

  public File getContactsFile() {
    return contactsFile;
  }

  public File getAccountsFile() {
    return accountsFile;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}