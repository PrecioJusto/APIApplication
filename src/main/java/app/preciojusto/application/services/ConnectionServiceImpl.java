package app.preciojusto.application.services;

import app.preciojusto.application.dto.BrandDTO;
import app.preciojusto.application.dto.CategoryDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.dto.mappers.ProductResponseMapper;
import com.google.gson.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.util.*;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Value("${apiproduct.endpoint}")
    String endpoint;

    @Override
    public List<ProductResponseDTO> getProducts(List<Long> ids) throws Exception {
        String json = new Gson().toJson(ids);
        String urlString = endpoint + "/products/idslist";
        URL url = new URL(urlString);
        String resp = doPost(url, json);
        List<Map<String, Object>> maps = new Gson().fromJson(resp, ArrayList.class);
        return this.productResponseMapper.mapperList(maps);
    }



    private String doPost(URL url, String body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());
        post.setHeader("Content-type", "application/json");
        HttpEntity stringEntity = new StringEntity(body);
        post.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }
}
