package ru.filit.mdma.crm.util.exception;

/**
 * Исключение - данные не найдены.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}