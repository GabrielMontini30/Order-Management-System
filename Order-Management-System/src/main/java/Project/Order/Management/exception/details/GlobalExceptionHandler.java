package Project.Order.Management.exception.details;

import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.exception.StockException;
import Project.Order.Management.exception.handler.ExceptionDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        ExceptionDetails exceptionDetails= ExceptionDetails.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDetails);
    }


    @ExceptionHandler(StockException.class)
    public ResponseEntity<ExceptionDetails> handleStockException(StockException exception, HttpServletRequest request){
        ExceptionDetails exceptionDetails= ExceptionDetails.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handlerGenericException(Exception exception, HttpServletRequest request){

        ExceptionDetails details = ExceptionDetails.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .error("Internal Server Error")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDetails> handlerIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest  request){
        ExceptionDetails details = ExceptionDetails.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .error("Bad Request")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
    }
}




