package ru.skypro.homework.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skypro.homework.exceptions.AdNoContentException;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.UserUnauthorizedException;

@ControllerAdvice
public class AdExceptionHandler {

    @ExceptionHandler(value = {AdNotFoundException.class})
    public ResponseEntity<?> handleAdNotFound(AdNotFoundException exception) {
        String message = "Объявление не найдено";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AdNoContentException.class})
    public ResponseEntity<?> handleAdNoContent(AdNoContentException exception) {
        String message = "Объявление удалено";
        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = {UserUnauthorizedException.class})
    public ResponseEntity<?> handleAdUserUnauthorized(UserUnauthorizedException exception) {
        String message = "Вы не авторизованы";
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

}