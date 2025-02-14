package shurona.wordfinder.custom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shurona.wordfinder.custom.dto.DeeplRequestDto;
import shurona.wordfinder.custom.dto.DeeplResponseDto;
import shurona.wordfinder.word.service.DeeplWordService;

@Service
public class WordExternalConnection {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final DeeplWordService deeplWordService;

    public WordExternalConnection(DeeplWordService deeplWordService) {
        this.deeplWordService = deeplWordService;
    }

    /**
     * 주어진 단어를 번역합니다.
     */
    public String translateWord(String word) {
        if (!StringUtils.hasText(word)) {
            log.error("번역할 단어가 비어있습니다.");
            throw new IllegalArgumentException("번역할 단어는 필수입니다.");
        }

        try {
            DeeplRequestDto request = new DeeplRequestDto(new String[]{word});
            DeeplResponseDto response = deeplWordService.getWordInfo(request);

            if (response == null || response.getTranslations() == null
                || response.getTranslations().isEmpty()) {
                return word;
            }

            String translatedText = response.getTranslations().get(0).getText();
            return translatedText;

        } catch (Exception e) {
            log.error("단어 번역 중 오류 발생. 단어: {}", word, e);
            throw new RuntimeException("번역 처리 중 오류가 발생했습니다.", e);
        }
    }
}

