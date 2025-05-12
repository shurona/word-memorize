package shurona.wordfinder.custom.dto.line;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WebHookRequestDto(
    @JsonProperty("destination") String destination,
    @JsonProperty("events") List<LineEvent> events
) {

}
