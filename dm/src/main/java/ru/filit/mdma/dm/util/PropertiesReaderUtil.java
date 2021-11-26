package ru.filit.mdma.dm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReaderUtil {

  private final Properties props = new Properties();

  public PropertiesReaderUtil(String fileProps) {
    try {
      loadProperties(fileProps);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadProperties(String fileProps) throws IOException {
    try (InputStream inpStream = this.getClass().getClassLoader().getResourceAsStream(fileProps)) {
      this.props.load(inpStream);
    }
  }

  public String get(String key) {
    return this.props.getProperty(key);
  }
}