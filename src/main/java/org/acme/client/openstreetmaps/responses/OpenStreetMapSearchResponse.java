package org.acme.client.openstreetmaps.responses;

import com.fasterxml.jackson.databind.JsonNode;

public record OpenStreetMapSearchResponse(
        String lat,
        String lon,
        GeoJsonResponse geojson
) {}

record GeoJsonResponse(
    String type,
    JsonNode coordinates
) {}