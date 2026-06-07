package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.client.openstreetmaps.enums.GeoJsonType;
import org.acme.client.openstreetmaps.responses.GeoJsonResponse;
import org.acme.client.openstreetmaps.responses.OpenStreetMapSearchResponse;
import org.acme.servicearea.ServiceArea;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ServiceAreaTest {

    @Inject
    EntityManager em;

    @Inject
    @RestClient
    OpenStreetMapsClient openStreetMapsClient;

    @Test
    @Transactional
    public void shouldCreateServiceAreaAndReturnCreatedPolygon() {
        Optional<OpenStreetMapSearchResponse> serviceAreaResult = openStreetMapsClient.search("Lisboa, Portugal", "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isPolygon())
                .findFirst();

        if (serviceAreaResult.isEmpty()) {
            throw new RuntimeException("No OpenStreetMaps found");
        }

        var serviceArea = new ServiceArea();
        serviceArea.setAddress("Lisboa, Portugal");
        serviceArea.setPolygon(serviceAreaResult.get().geojson().toJtsPolygon());
        em.persist(serviceArea);
        assertNotNull(serviceArea.getId());
    }
}
