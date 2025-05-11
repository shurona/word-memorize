package shurona.wordfinder.custom.dto.line;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LineSource(
    @JsonProperty("type") String type,
    @JsonProperty("userId") String userId
) {

}
