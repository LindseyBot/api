package net.notfab.lindsey.api.advice;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestfulErrorAdviser extends ResponseEntityExceptionHandler {

    private final HttpHeaders headers = new HttpHeaders();

    public RestfulErrorAdviser() {
        headers.add("Content-Type", "application/json");
    }

    public static void handleFilterException(HttpServletResponse response, Exception exception, int status) {
        try {
            JSONObject object = new JSONObject();
            object.put("code", status);
            object.put("message", exception.getMessage());
            response.getWriter().print(object.toString());
            response.addHeader("Content-Type", "application/json");
            response.setStatus(status);
        } catch (IOException ignored) {
            // Ignored
        }
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        JSONObject object = new JSONObject();
        object.put("code", 400);
        object.put("message", ex.getMessage());
        return handleExceptionInternal(ex, object.toString(), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalState(IllegalStateException ex, WebRequest request) {
        JSONObject object = new JSONObject();
        object.put("code", 400);
        object.put("message", ex.getMessage());
        return handleExceptionInternal(ex, object.toString(), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request) {
        JSONObject object = new JSONObject();
        object.put("code", 500);
        object.put("message", "Internal server error");
        logger.error("Internal Error", ex);
        return handleExceptionInternal(ex, object.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        JSONObject object = new JSONObject();
        object.put("code", 403);
        object.put("message", "Forbidden");
        return handleExceptionInternal(ex, object.toString(), headers, HttpStatus.FORBIDDEN, request);
    }

}
