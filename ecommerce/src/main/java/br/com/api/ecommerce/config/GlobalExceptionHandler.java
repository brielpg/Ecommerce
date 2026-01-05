package br.com.api.ecommerce.config;

import br.com.api.ecommerce.exceptions.AccessDeniedException;
import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.dtos.ErrorDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        ErrorDto errorDto = new ErrorDto(httpStatus.value(), httpStatus.name(), List.of(errorMessage));
        return ResponseEntity.status(httpStatus).body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        ErrorDto errorDto = new ErrorDto(httpStatus.value(), httpStatus.name(), List.of(errorMessage));
        return ResponseEntity.status(httpStatus).body(errorDto);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDto> handleConflictException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        ErrorDto errorDto = new ErrorDto(httpStatus.value(), httpStatus.name(), List.of(errorMessage));
        return ResponseEntity.status(httpStatus).body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        ErrorDto errorDto = new ErrorDto(httpStatus.value(), httpStatus.name(), List.of(errorMessage));
        return ResponseEntity.status(httpStatus).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
                    String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
                    return fieldName + ": " + errorMessage;
                }).toList();

        ErrorDto errorDto = new ErrorDto(httpStatus.value(), httpStatus.name(), errors);
        return ResponseEntity.status(httpStatus).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
