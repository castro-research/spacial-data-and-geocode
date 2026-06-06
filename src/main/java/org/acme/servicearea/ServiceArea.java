package org.acme.servicearea;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.locationtech.jts.geom.Polygon;

@Entity
public class ServiceArea {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    String address;

    @Column(nullable = false, columnDefinition = "geometry(Polygon, 4326)")
    Polygon coverageArea;
}
