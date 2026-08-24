package org.acme.client.openstreetmaps.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.client.openstreetmaps.enums.GeoJsonType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

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

    public boolean isArea() {
        return type == GeoJsonType.POLYGON || type == GeoJsonType.MULTIPOLYGON;
    }

    public MultiPolygon toJtsMultiPolygon() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        ObjectMapper mapper = new ObjectMapper();

        if (type == GeoJsonType.MULTIPOLYGON) {
            double[][][][] polygons = mapper.convertValue(coordinates, double[][][][].class);
            Polygon[] jtsPolygons = Arrays.stream(polygons)
                    .map(rings -> toPolygon(geometryFactory, rings[0]))
                    .toArray(Polygon[]::new);
            return geometryFactory.createMultiPolygon(jtsPolygons);
        }

        if (type == GeoJsonType.POLYGON) {
            double[][][] rings = mapper.convertValue(coordinates, double[][][].class);
            return geometryFactory.createMultiPolygon(new Polygon[]{toPolygon(geometryFactory, rings[0])});
        }

        return geometryFactory.createMultiPolygon();
    }

    private static Polygon toPolygon(GeometryFactory geometryFactory, double[][] ring) {
        Coordinate[] coordinates = Arrays.stream(ring)
                .map(p -> new Coordinate(p[0], p[1]))
                .toArray(Coordinate[]::new);
        return geometryFactory.createPolygon(coordinates);
    }
}
