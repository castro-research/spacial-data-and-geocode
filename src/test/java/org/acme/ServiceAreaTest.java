package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.servicearea.ServiceArea;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ServiceAreaTest {

    @Inject
    @RestClient
    OpenStreetMapsClient openStreetMapsClient;

    @Test
    public void shouldCreateServiceAreaAndReturnCreatedPolygon() {
        var osmClient = openStreetMapsClient.search("Lisboa, Portugal", "json", 1);

        var serviceArea = new ServiceArea();

    }
}
