package shurona.wordfinder.custom.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shurona.wordfinder.common.dto.ApiResponse;
import shurona.wordfinder.custom.dto.TranslateResponseDto;
import shurona.wordfinder.custom.service.WordExternalConnection;

@RestController
@RequestMapping("connection")
public class ConnectionController {

    private final WordExternalConnection wordExternalConnection;


    @Autowired
    public ConnectionController(WordExternalConnection wordExternalConnection) {
        this.wordExternalConnection = wordExternalConnection;
    }

    /**
     * 변역 해주는 api
     */
    @GetMapping("translate")
    public ApiResponse<TranslateResponseDto> checkConnectionPool(
        @RequestParam(value = "word") String word,
        HttpServletResponse response
    ) throws IOException {
        // strip 적용
//        String output = this.wordExternalConnection.translateWord(word.strip());
        String output = null;
        if (output == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input word is wrong");
            return ApiResponse.createFail("Translate fail");
        }

        return ApiResponse.createSuccess(new TranslateResponseDto(word, output));
    }

}
