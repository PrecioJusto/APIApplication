package app.preciojusto.application.conf;

import app.preciojusto.application.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/profile",
                        "/api/shoppingcarts", "/api/shoppingcart", "/api/shoppingcart/*",
                        "/api/shoppingcartproduct", "/api/shoppingcartproduct/*/*",
                        "/api/favourite", "/api/favourite/*/*")
                .excludePathPatterns("/api/login", "/api/register");
    }
}