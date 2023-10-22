package ru.aston.learn.cat.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.aston.learn.cat.client.response.CatImageResponse;
import ru.aston.learn.cat.service.ObjectMapperService;

public class CatClient implements AutoCloseable {

    private static CatClient instance;

    public static CatClient getInstance() {
        if (instance == null) {
            instance = new CatClient();
        }
        return instance;
    }

    private final static String SCHEMA = "https";
    private final static String HOST = "api.thecatapi.com";
    private final static String API_KEY = "live_rAudCrpbybysVuWOT7hCrHhqvUEFrNLxhNcuSU0WkLe7ivyj9dIZtnMsMA306V51";

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper = ObjectMapperService.getInstance();

    private CatClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public List<CatImageResponse> getCatImages(int cnt) {
        try {
            URI uri = new URIBuilder()
                    .setScheme(SCHEMA)
                    .setHost(HOST)
                    .setPath("v1/images/search")
                    .addParameter("api_key", API_KEY)
                    .addParameter("limit", String.valueOf(cnt))
                    .addParameter("has_breeds", "1")
                    .build();

            HttpGet request = new HttpGet(uri);

            CloseableHttpResponse response = httpClient.execute(request);

            return objectMapper.readValue(
                    new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8),
                    new TypeReference<>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        httpClient.close();
    }
}
