package org.acme.client.openstreetmaps.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.client.openstreetmaps.enums.GeoJsonType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.Arrays;

public class GeoJsonResponse {
    GeoJsonType type;
    JsonNode coordinates;

    public GeoJsonResponse(
            @JsonProperty("type") GeoJsonType type,
            @JsonProperty("coordinates") JsonNode coordinates
    ) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public double[][][] getPolygons() {
        if (!isPolygon()) return null;

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(coordinates, double[][][].class);
    }

    public boolean isPolygon() {
        return type.equals(GeoJsonType.POLYGON);
    }

    public Polygon toJtsPolygon() {
        GeometryFactory geometryFactory = new GeometryFactory();
        if (!isPolygon()) {
            return geometryFactory.createPolygon();
        }

        double[][] ring = getPolygons()[0];

        Coordinate[] coordinates = Arrays.stream(ring)
                .map(p -> new Coordinate(p[0], p[1]))
                .toArray(Coordinate[]::new);

        return geometryFactory.createPolygon(coordinates);
    }
}