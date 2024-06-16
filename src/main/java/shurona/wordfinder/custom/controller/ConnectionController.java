package shurona.wordfinder.custom.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shurona.wordfinder.common.dto.ApiResponse;
import shurona.wordfinder.custom.dto.TranslateResponseDto;
import shurona.wordfinder.custom.service.ConnectionTestService;

import java.io.IOException;

@RestController
@RequestMapping("connection")
public class ConnectionController {

    private final ConnectionTestService connectionTestService;


    @Autowired
    public ConnectionController(ConnectionTestService connectionTestService) {
        this.connectionTestService = connectionTestService;
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
        String output = this.connectionTestService.getProperty(word.strip());
//        String output =null;
        if (output == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input word is wrong");
            return ApiResponse.createFail("Translate fail");
        }

        return ApiResponse.createSuccess(new TranslateResponseDto(word, output));
    }

}
