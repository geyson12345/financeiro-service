package br.com.finchsolucoes.financeiro.service.core.handlers;


import br.com.finchsolucoes.financeiro.service.core.dtos.*;
import br.com.finchsolucoes.financeiro.service.core.handlers.exception.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.*;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.exception.*;
import org.springframework.beans.*;
import org.springframework.context.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.lang.*;
import org.springframework.validation.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;


    public ApplicationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;

    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(@NonNull HttpMediaTypeNotAcceptableException ex,
                                                                      @NonNull HttpHeaders headers,
                                                                      @NonNull HttpStatusCode status,
                                                                      @NonNull WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }


    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.info("M=Exception", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.ERRO_DE_SISTEMA), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatusCode status,
                                                                   @NonNull WebRequest request) {
        log.info("M=NoHandlerFoundException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.RECURSO_NAO_ENCONTRADO), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatusCode status,
                                                        @NonNull WebRequest request) {
        log.info("M=TypeMismatchException", ex);
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.info("M=HttpMessageNotReadableException", ex);
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleAccessDeniedException(BusinessException ex, WebRequest request) {
        log.info("M=BussinessExcpetion", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.ERRO_NEGOCIO), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex, WebRequest request) {
        log.info("M=EntityNotFoundException", ex);
        HttpStatus status = NOT_FOUND;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.ENTIDADE_NAO_ENCONTRADA), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailableException(final EntityNotFoundException ex, WebRequest request) {
        log.info("M=EntityNotFoundException", ex);
        HttpStatus status = SERVICE_UNAVAILABLE;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.ERRO_DE_SISTEMA), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(final UnauthorizedAccessException ex, WebRequest request) {
        log.info("M=UnauthorizedAccessException", ex);
        HttpStatus status = UNAUTHORIZED;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.SEM_AUTORIZACAO), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(final InternalServerErrorException ex, WebRequest request) {
        log.info("M=InternalServerErrorException", ex);
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.SERVER_ERROR), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ForbidenException.class)
    public ResponseEntity<Object> handleForbidenException(final ForbidenException ex, WebRequest request) {
        log.info("M=ForbidenException", ex);
        HttpStatus status = FORBIDDEN;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.SEM_AUTORIZACAO), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, WebRequest request) {
        log.info("M=BadRequestException", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO error = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.ERRO_NEGOCIO), ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatusCode status,
                                                            WebRequest request,
                                                            BindingResult bindingResult) {
        String detail = Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED);
        List<ErrorDetailsDTO.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message;
                    try {
                        message = messageSource.getMessage(Objects.requireNonNull(objectError.getDefaultMessage()), null, Util.getLocaleDefault());
                    } catch (NoSuchMessageException e) {
                        message = objectError.getDefaultMessage();
                    }
                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }
                    return ErrorDetailsDTO.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                }).toList();
        ErrorDetailsDTO problem = createProblemBuilder(status,
                Util.retornaMensagem(TitleValidationConstants.DADOS_INVALIDOS),
                detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        problem.setObjects(problemObjects);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ErrorDetailsDTO createProblemBuilder(HttpStatusCode status, String title, String detail, String contextPah) {
        return ErrorDetailsDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(contextPah)
                .title(title)
                .detail(detail).build();
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String detail = String.format(Util.retornaMensagem(MessageConstants.PARAMETES_INVALIDATED, ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.PARAMETRO_INVALIDO), detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers,
                                                       HttpStatusCode status,
                                                       WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format(Util.retornaMensagem(MessageConstants.PROPERTIES_INVALIDATED, path, ex.getValue(), ex.getTargetType().getSimpleName()));
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL), detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers,
                                                         HttpStatusCode status,
                                                         WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format(Util.retornaMensagem(MessageConstants.PROPERTIE_NOTFOUND, path));
        ErrorDetailsDTO problem = createProblemBuilder(status, Util.retornaMensagem(TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL), detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException ex, WebRequest request) {
        log.info("M=EntityAlreadyExistsException", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getDescription(false);
        ErrorDetailsDTO error = createProblemBuilder(
                status,
                Util.retornaMensagem(TitleValidationConstants.ERRO_NEGOCIO),
                ex.getMessage(),
                path
        );
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }


}
