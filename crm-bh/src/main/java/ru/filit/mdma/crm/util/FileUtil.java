package ru.filit.mdma.crm.util;

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
  private final PropertiesReaderUtil propsReader = new PropertiesReaderUtil(
      "storage.properties");

  private final String USERS_FILE = propsReader.get("users.path");
  private final File usersFile = new File(USERS_FILE);

  public File getUsersFile() {
    return usersFile;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}