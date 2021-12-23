package ru.filit.mdma.dms.service;

import java.util.Locale;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.stereotype.Service;

/**
 * Сервис по сохранению и получению токенов с помощью Apache Ignite.
 */
@Slf4j
@Service
public class TokenCacheService {

  private Ignite ignite;

  public TokenCacheService(Ignite ignite) {
    this.ignite = ignite;
  }

  /**
   * Cохранение токена на основе значения маскируемого поля.
   */
  public String saveTokenByValue(String value) {
    UUID uuid = UUID.randomUUID();
    String randomGUIDString =
        "#" + uuid.toString().toUpperCase(Locale.ROOT).replaceAll("-", "") + "#";
    log.info("Сохранение значения маскируемого поля под GUID: {}", randomGUIDString);
    getTokenCache().put(randomGUIDString, value);

    return randomGUIDString;
  }

  /**
   * Получение значения маскируемого поля на основе токена.
   */
  public String getValueByToken(String token) {
    log.info("Получение значения маскируемого поля под GUID: {}", token);

    return getTokenCache().get(token);
  }

  private IgniteCache<String, String> getTokenCache() {

    return ignite.cache("tokenCache");
  }
}