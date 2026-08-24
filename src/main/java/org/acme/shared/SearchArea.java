package org.acme.shared;

import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTWriter;

public record SearchArea(MultiPolygon area) {
    public String wkt() {
        return new WKTWriter().write(area);
    }
}
