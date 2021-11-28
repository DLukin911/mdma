package ru.filit.mdma.dm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Запуск приложения DataManager.
 */
@SpringBootApplication
@EnableCaching
public class DataManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DataManagerApplication.class, args);
  }
}