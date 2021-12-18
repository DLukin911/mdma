package ru.filit.mdma.dms.util.exception;

/**
 * Исключение - данные не найдены.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}