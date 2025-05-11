package shurona.wordfinder.custom.dto.line;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LineMessage(
    @JsonProperty("type") String type,
    @JsonProperty("id") String id,
    @JsonProperty("text") String text
) {

}