package ok.serge.taskmanagementsystem.advice;

import lombok.extern.slf4j.Slf4j;
import ok.serge.taskmanagementsystem.annotation.OptimisticLock;
import ok.serge.taskmanagementsystem.exception.TaskNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = OptimisticLock.class)
@Slf4j
public class TaskControllerAdvice {

    private static final String ERROR_KEY = "Ошибка";

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                Map.of(ERROR_KEY, "Попытка изменения или удаления неактуальной версии задачи"),
                new HttpHeaders(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of(ERROR_KEY, ex.getMessage()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                Map.of(ERROR_KEY, errorMessage),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                Map.of(ERROR_KEY, "Неизвестная ошибка"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }
}
