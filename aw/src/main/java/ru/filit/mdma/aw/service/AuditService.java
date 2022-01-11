package ru.filit.mdma.aw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.filit.mdma.aw.repository.AuditRepository;

/**
 * Сервис получения сообщений из брокера Kafka.
 */
@Service
@Slf4j
public class AuditService {

  private AuditRepository auditRepository;

  public AuditService(AuditRepository auditRepository) {
    this.auditRepository = auditRepository;
  }

  @KafkaListener(groupId = "one", topics = "dm-audit")
  void listener(String message) {
    log.info("Сообщение получено [{}]", message);

    auditRepository.saveAuditResult(message);
  }
}