package sleepy.mollu.server.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import sleepy.mollu.server.oauth2.controller.parser.AuthorizationHeaderParser;
import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;
import sleepy.mollu.server.oauth2.jwt.service.JwtExtractor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    private static final String ATTRIBUTE_TIME = "time";

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put("userId", getUserId(request));
        MDC.put("requestId", UUID.randomUUID().toString());

        final long start = System.currentTimeMillis();
        request.setAttribute(ATTRIBUTE_TIME, start);

        log.info("Request URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
        final Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> log.info("Request Parameter: {}={}", key, value));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response Status: {}", response.getStatus());

        final long start = (long) request.getAttribute(ATTRIBUTE_TIME);
        final long end = System.currentTimeMillis();

        log.info("[" + handler + "] executeTime : " + (end - start) + "ms");

        MDC.clear();
    }

    private String getUserId(HttpServletRequest request) {
        try {
            final String accessToken = AuthorizationHeaderParser.parse(request);
            final JwtPayload payload = jwtExtractor.extract(accessToken, ExtractType.ACCESS);
            return payload.id();
        } catch (Exception e) {
            return null;
        }
    }
}
