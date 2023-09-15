package com.medhead.POC.dataInterfaces.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Distance(String text, int value) {}
