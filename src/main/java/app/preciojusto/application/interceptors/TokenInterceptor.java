package app.preciojusto.application.interceptors;

import app.preciojusto.application.services.TokenService;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) return true;
        String header = request.getHeader("Authorization");


        if (header == null || header.equals("Bearer null")) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized");
            return false;
        }

        try {
            String token = header.replace("Bearer ", "");
            Map<String, Claim> claimUser = tokenService.getSubject(token);
            request.setAttribute("userToken", claimUser);
        } catch (Exception e) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized");
            return false;
        }
        return true;
    }
}
