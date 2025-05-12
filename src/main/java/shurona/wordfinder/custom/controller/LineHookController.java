package shurona.wordfinder.custom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 객체를 JSON 문자열로 변환 (Node.js의 JSON.stringify와 동일)
            String json = objectMapper.writeValueAsString(requestBody);

            System.out.println("사이즈는 : " + requestBody.events().size());

            System.out.println(json); // 혹은 로그로 출력

            System.out.println(
                objectMapper.writeValueAsString(requestBody.events().get(0).message().get()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        // 예외 메시지 로그 남기기
        System.err.println("Missing header: " + ex.getHeaderName());

        // 클라이언트에 400 Bad Request와 메시지 전송
        return ResponseEntity
            .badRequest()
            .body("필수 헤더(" + ex.getHeaderName() + ")가 누락되었습니다.");
    }
}
