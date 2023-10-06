package com.medhead.POC.models.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Record to de-serialize Duration JSON object from Google distancematrix API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Duration(String text, int value) {
    public int GetValue() {
        return this.value;
    }
}
