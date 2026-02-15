package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e){
        var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(),e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(),"duplicated field for one of the unique field");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(),"the unique fields dont should be null");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
