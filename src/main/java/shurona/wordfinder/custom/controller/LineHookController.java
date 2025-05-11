package shurona.wordfinder.custom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import shurona.wordfinder.custom.dto.line.WebHookRequest;

@RestController
public class LineHookController {

    @PostMapping
    public ResponseEntity<?> checkUrl(
        @RequestHeader("x-line-signature") String header,
        @RequestBody WebHookRequest requestBody
    ) {

        System.out.println("?? : " + header);

        System.out.println(requestBody.destination());

        return ResponseEntity.ok().build();
    }

}
