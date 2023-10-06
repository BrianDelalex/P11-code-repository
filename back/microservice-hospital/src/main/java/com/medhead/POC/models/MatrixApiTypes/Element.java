package com.medhead.POC.models.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Record to de-serialize Element JSON object from Google distancematrix API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Element(Distance distance, Duration duration) {
    public Duration GetDuration() {
        return this.duration;
    }
}
