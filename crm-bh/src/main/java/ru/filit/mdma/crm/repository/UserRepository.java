package ru.filit.mdma.crm.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.crm.util.FileUtil;
import ru.filit.oas.crm.model.User;

/**
 * Класс для работы с базами данных на основе YAML файлов.
 */
@Slf4j
@Repository
public class UserRepository {

  private static Map<String, User> usersCache = new HashMap<>();

  static {
    enableUsersCache();
  }

  /**
   * Поиск данных пользователя по его username.
   */
  public User findByUsername(String username) {
    log.info("Запрос данных пользователя из базы данных по username = {}", username);
    User user = usersCache.get(username);

    return user;
  }

  /**
   * Активация кеша сущности Пользователь (id, User).
   */
  private static void enableUsersCache() {
    log.info("Запрос списка всех пользователей из базы данных");
    List<User> userList = null;
    try {
      userList = FileUtil.getMapper()
          .readValue(FileUtil.getUsersFile(), new TypeReference<List<User>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
    usersCache = userList.stream().collect(Collectors.toMap(User::getUsername, user -> user));
  }
}
