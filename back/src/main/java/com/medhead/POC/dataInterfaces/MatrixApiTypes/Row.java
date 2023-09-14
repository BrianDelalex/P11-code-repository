package com.medhead.POC.dataInterfaces.MatrixApiTypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Row(List<Element> elements) {
    public List<Element> GetElements() {
        return this.elements;
    }
}
