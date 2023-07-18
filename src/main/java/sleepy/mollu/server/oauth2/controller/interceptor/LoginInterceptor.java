package sleepy.mollu.server.oauth2.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import online.partyrun.jwtmanager.JwtExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sleepy.mollu.server.oauth2.controller.parser.AuthorizationHeaderParser;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        final String accessToken = AuthorizationHeaderParser.parse(request);
        jwtExtractor.extract(accessToken);

        return true;
    }
}
