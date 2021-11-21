package it.academy.by.befitapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoRightsForChangeException.class)
    public ResponseEntity<?> noRightsForChange(){
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.FORBIDDEN.value(),
                "У вас нет прав на внесение изменений в данный профиль",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy"))
        );
        return new ResponseEntity<>(exceptionMessage,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UpdateDeleteException.class)
    public ResponseEntity<?> updateOrDeleteIsNotAvailable(){
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.LOCKED.value(),
                "Внесение изменений или удаление временно заблокировано",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy"))
        );
        return new ResponseEntity<>(exceptionMessage,HttpStatus.LOCKED);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<?> elementNotFound(){
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                "Запрашиваемого элемента нет в базе данных",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy"))
        );
        return new ResponseEntity<>(exceptionMessage,HttpStatus.NOT_FOUND);
    }

   /* @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> otherExceptions(){
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Oops! Something goes wrong... We are trying to fix it!",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy"))
        );
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    */
}
