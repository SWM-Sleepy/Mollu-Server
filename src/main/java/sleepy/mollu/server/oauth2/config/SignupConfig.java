package sleepy.mollu.server.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sleepy.mollu.server.oauth2.controller.resolver.SocialTokenArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SignupConfig implements WebMvcConfigurer {

    private final SocialTokenArgumentResolver socialTokenArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(socialTokenArgumentResolver);
    }
}
