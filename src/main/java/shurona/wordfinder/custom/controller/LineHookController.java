package shurona.wordfinder.custom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineHookController {

    @PostMapping
    public ResponseEntity<?> checkUrl() {

        System.out.println("??");

        return ResponseEntity.ok().build();
    }

}
