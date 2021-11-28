package ru.filit.mdma.dm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import lombok.experimental.UtilityClass;

/**
 * Класс для чтения файлов.
 */
@UtilityClass
public class FileUtil {

  private PropertiesReaderUtil propsReader = new PropertiesReaderUtil("storage.properties");
  private String ENTITY_REPOSITORY_FILE = propsReader.get("entityRepository.path");
  private final File entityFile = new File(ENTITY_REPOSITORY_FILE);
  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

  public File getEntityFile() {
    return entityFile;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}