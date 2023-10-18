package sleepy.mollu.server.admin.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.service.JwtExtractor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            final Cookie[] cookies = request.getCookies();
            final String jwtToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("admin"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseThrow();

            jwtExtractor.extract(jwtToken, ExtractType.ACCESS);
            return true;

        } catch (Exception e) {
            response.sendRedirect("/api/admin/login");
            return false;
        }


    }
}
