package org.acme.servicearea;

import jakarta.persistence.*;
import org.acme.user.User;
import org.locationtech.jts.geom.MultiPolygon;

@Entity
@Table(name = "service_areas")
public class ServiceArea {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    String address;

    @Column(nullable = false, columnDefinition = "geometry(MultiPolygon, 4326)")
    MultiPolygon coverageArea;

    @ManyToOne
    User user;

    public Long getId() {
        return id;
    }
    public void setAddress(String address) { this.address = address; }
    public void setCoverageArea(MultiPolygon coverageArea) {
        this.coverageArea = coverageArea;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
