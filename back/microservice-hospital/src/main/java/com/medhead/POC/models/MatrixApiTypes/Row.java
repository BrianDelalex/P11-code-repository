package com.medhead.POC.models.MatrixApiTypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Record to de-serialize Row JSON object from Google distancematrix API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Row(List<Element> elements) {
    public List<Element> GetElements() {
        return this.elements;
    }
}
