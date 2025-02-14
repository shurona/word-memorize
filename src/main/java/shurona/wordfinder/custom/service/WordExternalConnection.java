package shurona.wordfinder.custom.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shurona.wordfinder.custom.dto.DeeplRequestDto;
import shurona.wordfinder.custom.dto.DeeplResponseDto;

@Service
public class WordExternalConnection {

    private final Logger log = LoggerFactory.getLogger(getClass());
    // 여기서 env 한 번 갖고 와 볼까
    private final Environment environment;

    @Autowired
    public WordExternalConnection(Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String word) {

        String base64ApiKey = this.environment.getProperty("deepl.api.key");
        String url = this.environment.getProperty("deepl.api.url");

        // 내부적 오류 발생 시 stop
        if (url == null || base64ApiKey == null) {
            this.log.error("Env가 제대로 들어오지 않습니다.");
            return null;
        }

        // UTF-8 문자열로 변환
        byte[] decodedBytes = Base64.getDecoder().decode(base64ApiKey);
        String apiKey = new String(decodedBytes, StandardCharsets.UTF_8);

        // Request 객체
        DeeplRequestDto deeplRequestDto = new DeeplRequestDto(new String[]{word});

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<DeeplRequestDto> requestEntity = new RequestEntity<>(deeplRequestDto, headers,
            HttpMethod.POST, URI.create(url));

        // 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DeeplResponseDto> exchange = restTemplate.exchange(requestEntity,
            DeeplResponseDto.class);

        // null 확인
        if (exchange.getBody() == null) {
            return null;
        }
        return exchange.getBody().getTranslations().get(0).getText();
    }


}

