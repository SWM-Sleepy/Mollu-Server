package sleepy.mollu.server.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    private static final String ATTRIBUTE_TIME = "time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final long start = System.currentTimeMillis();
        request.setAttribute(ATTRIBUTE_TIME, start);

        log.info("Request URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
        final Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> log.info("Request Parameter: {} = {}", key, value));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response Status: {}", response.getStatus());

        final long start = (long) request.getAttribute(ATTRIBUTE_TIME);
        final long end = System.currentTimeMillis();

        log.info("[" + handler + "] executeTime : " + (end - start) + "ms");
    }
}
