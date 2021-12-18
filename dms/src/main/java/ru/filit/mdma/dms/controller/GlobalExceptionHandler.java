package ru.filit.mdma.dms.controller;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.filit.mdma.dms.util.exception.NotFoundException;

/**
 * Перехватчик исключений веб приложения DMS.
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final ErrorAttributes errorAttributes;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> persistException(WebRequest request, NotFoundException nf) {
    log.error("NotFoundException: {}", nf.getMessage());
    return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> persistException(WebRequest request, Exception ex) {
    log.error("Internal error: {}", ex.getMessage());
    return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error("Internal error: {}", ex.getMessage());
    return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Map<String, Object> getDefaultBody(WebRequest request, ErrorAttributeOptions options,
      String msg) {
    Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
    if (msg != null) {
      body.put("message", msg);
    }
    return body;
  }

  @SuppressWarnings("unchecked")
  private <T> ResponseEntity<T> createResponseEntity(Map<String, Object> body, HttpStatus status) {
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
  }
}