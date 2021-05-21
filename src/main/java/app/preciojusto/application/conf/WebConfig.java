package app.preciojusto.application.conf;

import app.preciojusto.application.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/getprofile", "/editprofile", "/shoppingcart/*/user", "/shoppingcart/*/get", "/shoppingcart/add", "/shoppingcart/update", "/shoppingcart/*/delete");
    }
}