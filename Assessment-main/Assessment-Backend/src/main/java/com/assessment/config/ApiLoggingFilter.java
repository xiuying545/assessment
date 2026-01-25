package com.assessment.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class ApiLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper request =
                new ContentCachingRequestWrapper((HttpServletRequest) req, 1024 * 1024);
        ContentCachingResponseWrapper response =
                new ContentCachingResponseWrapper((HttpServletResponse) res);

        long startTime = System.currentTimeMillis();

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        String requestBody = new String(request.getContentAsByteArray());
        String responseBody = new String(response.getContentAsByteArray());

        log.info("API REQUEST → method={} uri={} body={}",
                request.getMethod(),
                request.getRequestURI(),
                requestBody);

        log.info("API RESPONSE → status={} duration={}ms body={}",
                response.getStatus(),
                duration,
                responseBody);

        response.copyBodyToResponse();
    }
}