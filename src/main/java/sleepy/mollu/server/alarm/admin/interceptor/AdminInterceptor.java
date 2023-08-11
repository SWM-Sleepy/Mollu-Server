package sleepy.mollu.server.alarm.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Object session = request.getSession().getAttribute("adminSession");
        if (session == null) {
            response.sendRedirect("/api/admin/login");
            return false;
        }

        return true;
    }
}
