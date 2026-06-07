package org.acme.client.openstreetmaps.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GeoJsonType {
    @JsonProperty("Polygon")
    POLYGON,
    @JsonProperty("MultiPolygon")
    MULTIPOLYGON
}
