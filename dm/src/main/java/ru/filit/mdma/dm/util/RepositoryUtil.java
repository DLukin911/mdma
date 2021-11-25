package ru.filit.mdma.dm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RepositoryUtil {

  private final String ENTITY_REPOSITORY_FILE = "src/main/resources/database/entity-repository.yaml";
  private final File entityFile = new File(ENTITY_REPOSITORY_FILE);
  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

  public File getEntityFile() {
    return entityFile;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}