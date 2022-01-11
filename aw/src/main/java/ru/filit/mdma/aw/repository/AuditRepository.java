package ru.filit.mdma.aw.repository;

import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Работа с данными аудита.
 */
@Slf4j
@Repository
public class AuditRepository {

  private static final String AUDIT_FILE_NAME = "aw/src/main/resources/datafiles/dm-audit.txt";

  /**
   * Сохранение в файл результатов аудита.
   */
  public static String saveAuditResult(String auditResult) {
    log.info("Сохранение результата аудита");

    try (FileWriter writer = new FileWriter(AUDIT_FILE_NAME, true)) {
      writer.write(auditResult + '\n');
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return auditResult;
  }
}