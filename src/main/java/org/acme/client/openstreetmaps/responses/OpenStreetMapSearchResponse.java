package org.acme.client.openstreetmaps.responses;

public record OpenStreetMapSearchResponse(
        String lat,
        String lon,
        GeoJsonResponse geojson
) {}
