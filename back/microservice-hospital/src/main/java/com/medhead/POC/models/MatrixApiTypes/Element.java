package com.medhead.POC.models.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Element(Distance distance, Duration duration) {
    public Duration GetDuration() {
        return this.duration;
    }
}
