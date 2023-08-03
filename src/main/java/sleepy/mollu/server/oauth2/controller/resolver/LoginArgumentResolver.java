package sleepy.mollu.server.oauth2.controller.resolver;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.oauth2.controller.parser.AuthorizationHeaderParser;
import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;
import sleepy.mollu.server.oauth2.jwt.service.JwtExtractor;

@Component
@RequiredArgsConstructor
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        final String accessToken = AuthorizationHeaderParser.parse((HttpServletRequest) webRequest.getNativeRequest());
        final JwtPayload jwtPayload = jwtExtractor.extract(accessToken, ExtractType.ACCESS);

        return jwtPayload.id();
    }
}
