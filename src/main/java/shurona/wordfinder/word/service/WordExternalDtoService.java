package shurona.wordfinder.word.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shurona.wordfinder.custom.service.WordExternalConnection;

/**
 * 외부 api를 접근할 때 사용하는 Service
 */
@Service
public class WordExternalDtoService {

    private final WordExternalConnection wordExternalConnection;
    private Logger log = LoggerFactory.getLogger(getClass());

    public WordExternalDtoService(WordExternalConnection wordExternelConnection) {
        this.wordExternalConnection = wordExternelConnection;
    }

    public String getMeaningInfo(String word) {
//        try {
//            throw new Exception();
//        } catch (Exception e) {
//            throw new RuntimeException("사...살려줘");
//        }

        String output = this.wordExternalConnection.getProperty(word);
        if (output.equals(word)) {
            this.log.info("입력과 뜻이 같은 단어 {}  {}", word, output);
            return null;
        }
        return output;
    }
}
