package sleepy.mollu.server.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sleepy.mollu.server.admin.interceptor.AdminInterceptor;
import sleepy.mollu.server.common.interceptor.WebInterceptor;
import sleepy.mollu.server.oauth2.controller.interceptor.LoginInterceptor;
import sleepy.mollu.server.oauth2.controller.resolver.LoginArgumentResolver;
import sleepy.mollu.server.oauth2.controller.resolver.RefreshTokenArgumentResolver;
import sleepy.mollu.server.oauth2.controller.resolver.SocialTokenArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebInterceptor webInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final AdminInterceptor adminInterceptor;

    private final SocialTokenArgumentResolver socialTokenArgumentResolver;
    private final LoginArgumentResolver loginArgumentResolver;
    private final RefreshTokenArgumentResolver refreshTokenArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger", "/swagger-ui/**", "/v3/api-docs/**")
                .excludePathPatterns("/auth/**", "/admin/**", "/actuator/**", "/bootstrap/**");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(socialTokenArgumentResolver);
        resolvers.add(loginArgumentResolver);
        resolvers.add(refreshTokenArgumentResolver);
    }
}
