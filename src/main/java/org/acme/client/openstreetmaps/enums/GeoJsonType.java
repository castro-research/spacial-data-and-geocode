package org.acme.client.openstreetmaps.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum GeoJsonType {
    @JsonProperty("Polygon")
    POLYGON,
    @JsonProperty("MultiPolygon")
    MULTIPOLYGON,

    UNKNOWN;

    @JsonCreator
    public static GeoJsonType fromValue(String value) {
        return switch (value) {
            case "Polygon" -> POLYGON;
            case "MultiPolygon" -> MULTIPOLYGON;
            default -> UNKNOWN;
        };
    }
}
