package shurona.wordfinder.word.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import shurona.wordfinder.word.service.DeeplWordService;

@Configuration
public class DeeplRestClientAdapter {

    private final String deeplApiKey;
    private final String deeplBaseUrl;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public DeeplRestClientAdapter(
        @Value("${deepl.api.url}") String deeplBaseUrl,
        @Value("${deepl.api.key}") String deeplApiKey
    ) {
        this.deeplApiKey = deeplApiKey;
        this.deeplBaseUrl = deeplBaseUrl;
        log.info("DeepL API URL: {}", deeplBaseUrl);
    }

    @Bean
    public DeeplWordService deeplWordService() {
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(createRestClient()))
            .build()
            .createClient(DeeplWordService.class);
    }

    private RestClient createRestClient() {
        String apiKeyUTF8 = new String(Base64.getDecoder().decode(deeplApiKey),
            StandardCharsets.UTF_8);

        return RestClient.builder()
            .baseUrl(deeplBaseUrl)
            .requestInterceptor(restClientInterceptor())
            .defaultHeaders(headers -> {
                headers.set("Authorization", "DeepL-Auth-Key " + apiKeyUTF8);
                headers.setContentType(MediaType.APPLICATION_JSON);
            })
            .build();
    }

    /*
        요청 URL 및 헤더 확인용 로그 인터셉터
     */
    private ClientHttpRequestInterceptor restClientInterceptor() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
//                log.info("DeepL API Request - URL: {}, Headers: {}",
//                    request.getURI(),
//                    request.getHeaders());
                return execution.execute(request, body);
            }
        };
    }
}
