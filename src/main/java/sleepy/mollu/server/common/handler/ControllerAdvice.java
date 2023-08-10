package sleepy.mollu.server.common.handler;

import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sleepy.mollu.server.common.dto.ExceptionResponse;
import sleepy.mollu.server.common.exception.*;

import java.util.stream.Collectors;

@RestControllerAdvice
//@Slf4j
public class ControllerAdvice {

    private static final String BAD_REQUEST_ERROR_MESSAGE = "잘못된 요청입니다.";
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(BAD_REQUEST_ERROR_MESSAGE));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindingExceptions(BindException exception) {
        log.error(exception.getClass().getName());
        final String message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> String.format("%s: %s", ((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        log.error(message);

        return ResponseEntity.badRequest().body(new ExceptionResponse(BAD_REQUEST_ERROR_MESSAGE));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(BAD_REQUEST_ERROR_MESSAGE));
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(UnAuthenticatedException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse("인증에 실패했습니다."));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationException(UnAuthorizedException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("권한이 없습니다."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse("요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionResponse> handleServletException(Exception exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(BAD_REQUEST_ERROR_MESSAGE));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ExceptionResponse> handleServerException(ServerException exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse("처리할 수 없는 예외입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse("처리할 수 없는 예외입니다."));
    }
}
