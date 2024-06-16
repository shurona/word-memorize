package shurona.wordfinder.custom.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 나만의 테스트를 할 때 넣어놓는 header auth
 */
@Component
public class HeaderAuthForDev implements HandlerInterceptor {

    private final Environment environment;

    @Autowired
    public HeaderAuthForDev(Environment environment) {
        this.environment = environment;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String rcvHeaderKey = request.getHeader("my-test-key");
        String devHeaderKey = this.environment.getProperty("dev.test.header");

        if (devHeaderKey != null && !devHeaderKey.equals(rcvHeaderKey)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
