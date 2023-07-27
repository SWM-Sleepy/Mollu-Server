package sleepy.mollu.server.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
        final Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> log.info("Request Parameter: {} = {}", key, value));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("Response Status: {}", response.getStatus());
    }
}
