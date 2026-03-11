package app.fitsync.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String LOG_TYPE = "logType";
    private static final String LOG_TYPE_VALUE = "http";
    private static final String HTTP_METHOD = "method";
    private static final String REQUEST_URI = "uri";
    private static final String RESPONSE_STATUS = "status";
    private static final String DURATION_MS = "durationMs";
    private static final String HTTP_LOG_MESSAGE = "HTTP request completed";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        String requestId = UUID.randomUUID().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        MDC.put(REQUEST_ID, requestId);
        MDC.put(LOG_TYPE, LOG_TYPE_VALUE);
        MDC.put(HTTP_METHOD, method);
        MDC.put(REQUEST_URI, uri);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            MDC.put(RESPONSE_STATUS, String.valueOf(status));
            MDC.put(DURATION_MS, String.valueOf(durationMs));

            log.info(HTTP_LOG_MESSAGE);

            MDC.remove(DURATION_MS);
            MDC.remove(RESPONSE_STATUS);
            MDC.remove(REQUEST_URI);
            MDC.remove(HTTP_METHOD);
            MDC.remove(REQUEST_ID);
        }
    }
}
