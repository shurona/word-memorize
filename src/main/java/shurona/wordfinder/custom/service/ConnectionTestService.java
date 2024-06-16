package shurona.wordfinder.custom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ConnectionTestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static class DeeplRequestDto {
        private String[] text;
        private String target_lang;
        private String source_lang;

        public DeeplRequestDto(String[] text) {
            this.text = text;
            this.target_lang = "KO";
            this.source_lang = "EN";
        }

        public String[] getText() {
            return text;
        }

        public String getTarget_lang() {
            return target_lang;
        }

        public String getSource_lang() {
            return source_lang;
        }

        @Override
        public String toString() {
            return "DeeplRequestDto{" +
                    "text=" + Arrays.toString(text) +
                    ", target_lang='" + target_lang + '\'' +
                    ", source_lang='" + source_lang + '\'' +
                    '}';
        }
    }

    private static class DeeplResponseDto {

        private List<Translation> translations;

        public static class Translation {
            private String detected_source_language;

            private String text;

            public String getDetected_source_language() {
                return detected_source_language;
            }

            public void setDetected_source_language(String detected_source_language) {
                this.detected_source_language = detected_source_language;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return "Translation{" +
                        "detected_source_language='" + detected_source_language + '\'' +
                        ", text='" + text + '\'' +
                        '}';
            }
        }

        //============================================================================================================
        public List<Translation> getTranslations() {
            return translations;
        }

        public void setTranslations(List<Translation> translations) {
            this.translations = translations;
        }

        @Override
        public String toString() {
            return "DeeplResponseDto{" +
                    "translations=" + translations +
                    '}';
        }
    }

    // 여기서 env 한 번 갖고 와 볼까
    private final Environment environment;

    @Autowired
    public ConnectionTestService(Environment environment) {
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
        RequestEntity<DeeplRequestDto> requestEntity = new RequestEntity<>(deeplRequestDto,headers, HttpMethod.POST, URI.create(url));

        // 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DeeplResponseDto> exchange = restTemplate.exchange(requestEntity, DeeplResponseDto.class);

        // null 확인
        if (exchange.getBody() == null) {
            return null;
        }
        return exchange.getBody().getTranslations().get(0).getText();
    }
}

