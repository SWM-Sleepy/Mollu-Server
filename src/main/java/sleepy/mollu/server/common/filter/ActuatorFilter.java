package sleepy.mollu.server.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ActuatorFilter implements Filter {

    private static final List<String> ALLOWED_IP_ADDRESSES = List.of("127.0.0.1", "0:0:0:0:0:0:0:1", "172.18.0.2");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        final String remoteAddr = httpServletRequest.getRemoteAddr();

        if (!ALLOWED_IP_ADDRESSES.contains(remoteAddr)) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
