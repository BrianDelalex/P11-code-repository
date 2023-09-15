package com.medhead.POC.models.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Duration(String text, int value) {
    public int GetValue() {
        return this.value;
    }
}
