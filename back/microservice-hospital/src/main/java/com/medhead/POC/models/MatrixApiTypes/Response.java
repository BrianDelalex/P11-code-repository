package com.medhead.POC.models.MatrixApiTypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Response(List<String> destination_addresses, List<String> origin_addresses, List<Row> rows) {
    public int GetDuration() {
        return this.rows.get(0).GetElements().get(0).GetDuration().GetValue();
    }
}
