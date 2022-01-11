package ru.filit.mdma.aw.repository;

import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Работа с данными аудита.
 */
@Slf4j
@Repository
public class AuditRepository {

  @Value("${audit.file.name}")
  private String auditFileName;

  /**
   * Сохранение в файл результатов аудита.
   */
  public String saveAuditResult(String auditResult) {
    log.info("Сохранение результата аудита");

    try (FileWriter writer = new FileWriter(auditFileName, true)) {
      writer.write(auditResult + '\n');
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return auditResult;
  }
}