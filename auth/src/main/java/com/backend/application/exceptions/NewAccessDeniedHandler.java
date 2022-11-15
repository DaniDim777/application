package com.backend.application.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class NewAccessDeniedHandler implements AccessDeniedHandler {

    @ExceptionHandler
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            ObjectMapper mapper = new ObjectMapper();
            NewAccessDeniedMessage newAccessDeniedMessage = new NewAccessDeniedMessage();
            newAccessDeniedMessage.setApplication_info("No Permissions");
            mapper.writeValue(response.getOutputStream(), newAccessDeniedMessage);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
