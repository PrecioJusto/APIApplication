package app.preciojusto.application.services;

import java.net.URL;
import java.util.Map;

public interface GoogleService {
    Map<String, String> getUserDetails(String accessToken) throws Exception;
    String getAccessToken(String code) throws Exception;
    URL getGoogleRedirectURL() throws Exception;
}
