package shurona.wordfinder.word.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import shurona.wordfinder.custom.dto.DeeplRequestDto;
import shurona.wordfinder.custom.dto.DeeplResponseDto;

@HttpExchange
public interface DeeplWordService {

    @PostExchange
    DeeplResponseDto getWordInfo(@RequestBody DeeplRequestDto requestDto);

}
