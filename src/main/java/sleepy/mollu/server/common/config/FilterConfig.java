package sleepy.mollu.server.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sleepy.mollu.server.common.filter.ActuatorFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ActuatorFilter> actuatorFilter() {
        final FilterRegistrationBean<ActuatorFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ActuatorFilter());
        registrationBean.addUrlPatterns("/actuator/*");
        return registrationBean;
    }
}
