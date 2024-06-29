package shurona.wordfinder.word.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shurona.wordfinder.custom.service.ConnectionTestService;

/**
 * 외부 api를 접근할 때 사용하는 Service
 */
@Service
public class WordExternalDtoService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private final ConnectionTestService connectionTestService;

    public WordExternalDtoService(ConnectionTestService connectionTestService) {
        this.connectionTestService = connectionTestService;
    }

    public String getMeaningInfo(String word) {
        String output = this.connectionTestService.getProperty(word);
        if (output.equals(word)) {
            this.log.info("입력과 뜻이 같은 단어 {}  {}", word, output);
            return null;
        }
        return output;
    }
}
