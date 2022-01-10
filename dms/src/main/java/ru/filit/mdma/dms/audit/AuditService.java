package ru.filit.mdma.dms.audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис подготовки данных для системы Аудита.
 */
@Service
@Slf4j
public class AuditService {

  private static int requestIdNum = 0;

  /**
   * Подготовка данных для аудита.
   */
  public static String createAuditRecord(String body, String requestId, String userName,
      String methodName) {
    log.info("Подготовка данных для аудита, метод: {}", methodName);

    String currentTime = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));

    if (requestId.length() < 8) {
      requestId = "0".repeat(8 - requestId.length()) + requestId;
    } else if (requestId.length() > 8) {
      requestIdNum = 1;
      requestId = "0".repeat(7) + requestIdNum;
    }

    if (userName.length() < 15) {
      userName = userName + " ".repeat(15 - userName.length());
    } else if (userName.length() > 15) {
      userName = userName.substring(0, 14);
    }

    if (methodName.length() < 25) {
      methodName = methodName + " ".repeat(25 - methodName.length());
    } else if (methodName.length() > 25) {
      methodName = methodName.substring(0, 24);
    }

    return currentTime + " " + requestId + " " + userName + " " + methodName + " " + body;
  }

  /**
   * Создание номера запроса.
   */
  public static String createRequestId() {
    requestIdNum++;

    return String.valueOf(requestIdNum);
  }
}