package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.servicearea.ServiceArea;
import org.acme.support.OsmServiceAreaFactory;
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
        Optional<ServiceArea> serviceAreaResult = OsmServiceAreaFactory.buildServiceArea(openStreetMapsClient, "Lisboa, Portugal");

        if (serviceAreaResult.isEmpty()) {
            throw new RuntimeException("No OpenStreetMaps found");
        }

        var serviceArea = serviceAreaResult.get();
        em.persist(serviceArea);
        assertNotNull(serviceArea.getId());
    }
}
