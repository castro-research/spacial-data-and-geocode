package org.acme.user;

import jakarta.persistence.*;
import org.acme.servicearea.ServiceArea;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ServiceArea> serviceAreas = new ArrayList<>();

    public void addServiceArea(ServiceArea serviceArea) {
        serviceArea.setUser(this);
        serviceAreas.add(serviceArea);
    }
}
