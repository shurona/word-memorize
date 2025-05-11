package shurona.wordfinder.custom.dto.line;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryContext(
    @JsonProperty("isRedelivery") boolean isRedelivery
) {

}
