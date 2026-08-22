package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.client.openstreetmaps.responses.OpenStreetMapSearchResponse;
import org.acme.servicearea.ServiceArea;
import org.acme.user.User;
import org.acme.user.UserRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserTest {
    @Inject
    EntityManager em;

    @Inject
    UserRepository userRepository;

    @Inject
    @RestClient
    OpenStreetMapsClient openStreetMapsClient;

    @Test
    @Transactional
    public void shouldCreateUserWithinServiceAreaAndBeFoundWithinPoint() throws Exception {
        Optional<OpenStreetMapSearchResponse> serviceAreaResult = openStreetMapsClient.search("Lisboa, Portugal", "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isPolygon())
                .findFirst();

        Optional<OpenStreetMapSearchResponse> searchPoint = openStreetMapsClient.search("Amadora, Lisboa, Portugal", "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isPolygon())
                .findFirst();

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        var serviceArea = new ServiceArea();
        serviceArea.setAddress("Lisboa, Portugal");
        serviceArea.setPolygon(serviceAreaResult.get().geojson().toJtsPolygon());
        user.addServiceArea(serviceArea);
        em.persist(user);

        double lat = Double.parseDouble(searchPoint.get().lat());
        double lnt = Double.parseDouble(searchPoint.get().lon());
        List<User> users = userRepository.findUserWithinRadius(lat, lnt, 3);
        assertEquals(1, users.size());
    }

    @Test
    @Transactional
    public void shouldCreateUserOutsideServiceAreaAndBeFoundWithinPoint() throws Exception {
        Optional<OpenStreetMapSearchResponse> serviceAreaResult = openStreetMapsClient.search("Campo Grande, Rio de Janeiro, Brasil", "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isPolygon())
                .findFirst();

        Optional<OpenStreetMapSearchResponse> searchPoint = openStreetMapsClient.search("Santa Cruz, Rio de Janeiro, Brasil", "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isPolygon())
                .findFirst();

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        var serviceArea = new ServiceArea();
        serviceArea.setAddress("Minha Quebrada");
        serviceArea.setPolygon(serviceAreaResult.get().geojson().toJtsPolygon());
        user.addServiceArea(serviceArea);
        em.persist(user);

        double lat = Double.parseDouble(searchPoint.get().lat());
        double lnt = Double.parseDouble(searchPoint.get().lon());
        List<User> users = userRepository.findUserWithinRadius(lat, lnt, 3);
        assertEquals(0, users.size());
    }
}
