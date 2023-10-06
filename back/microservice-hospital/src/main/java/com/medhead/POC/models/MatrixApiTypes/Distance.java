package com.medhead.POC.models.MatrixApiTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Record to de-serialize Distance JSON object from Google distancematrix API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Distance(String text, int value) {}
