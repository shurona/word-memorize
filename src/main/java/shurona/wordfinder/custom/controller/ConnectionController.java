package shurona.wordfinder.custom.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public String checkConnectionPool(
            @RequestParam(value = "word") String word,
            HttpServletResponse response
    ) throws IOException {
        String output = this.connectionTestService.getProperty(word);
        if (output == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input word is wrong");
        }
        return output;
    }

}
