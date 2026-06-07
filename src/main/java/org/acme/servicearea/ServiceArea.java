package org.acme.servicearea;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name = "service_areas")
public class ServiceArea {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    String address;

    @Column(nullable = false, columnDefinition = "geometry(Polygon, 4326)")
    Polygon coverageArea;

    public Long getId() {
        return id;
    }
    public void setAddress(String address) { this.address = address; }
    public void setPolygon(Polygon polygons) {
        this.coverageArea = polygons;
    }
}
