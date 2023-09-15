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

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    private static final String ATTRIBUTE_TIME = "time";
    private static final List<String> UPDATE_METHODS = List.of(HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name());

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put("uuid", UUID.randomUUID().toString());

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

        ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
        if (canLogResponse(request, responseWrapper)) {
            log.info("Response Body: {}", objectMapper.readTree(responseWrapper.getContentAsByteArray()));
        }


        final long start = (long) request.getAttribute(ATTRIBUTE_TIME);
        final long end = System.currentTimeMillis();

        log.info("[" + handler + "] executeTime : " + (end - start) + "ms");

        MDC.remove("uuid");
    }

    private boolean canLogResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        if (!UPDATE_METHODS.contains(request.getMethod())) {
            return false;
        }

        if (!isSuccess(response.getStatus())) {
            return false;
        }

        if (response.getContentType() == null) {
            return false;
        }

        if (!response.getContentType().contains("application/json")) {
            return false;
        }

        return response.getContentAsByteArray().length != 0;
    }

    private boolean isSuccess(int status) {
        return HttpStatus.valueOf(status).is2xxSuccessful();
    }
}
