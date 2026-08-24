package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.client.openstreetmaps.responses.OpenStreetMapSearchResponse;
import org.acme.servicearea.ServiceArea;
import org.acme.shared.SearchArea;
import org.acme.support.OsmServiceAreaFactory;
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
        Optional<ServiceArea> serviceAreaResult = OsmServiceAreaFactory.buildServiceArea(openStreetMapsClient, "Lisboa, Portugal");
        Optional<OpenStreetMapSearchResponse> searchPoint = OsmServiceAreaFactory.search(openStreetMapsClient, "Amadora, Lisboa, Portugal");

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        user.addServiceArea(serviceAreaResult.get());
        em.persist(user);

        double lat = OsmServiceAreaFactory.parseLat(searchPoint.get());
        double lnt = OsmServiceAreaFactory.parseLon(searchPoint.get());
        List<User> users = userRepository.findUserWithinRadius(lat, lnt, 3);
        assertEquals(1, users.size());
    }

    @Test
    @Transactional
    public void shouldCreateUserOutsideServiceAreaAndBeFoundWithinPoint() throws Exception {
        Optional<ServiceArea> serviceAreaResult = OsmServiceAreaFactory.buildServiceArea(
                openStreetMapsClient, "Campo Grande, Rio de Janeiro, Brasil", "Minha Quebrada");
        Optional<OpenStreetMapSearchResponse> searchPoint = OsmServiceAreaFactory.search(openStreetMapsClient, "Santa Cruz, Rio de Janeiro, Brasil");

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        user.addServiceArea(serviceAreaResult.get());
        em.persist(user);

        double lat = OsmServiceAreaFactory.parseLat(searchPoint.get());
        double lnt = OsmServiceAreaFactory.parseLon(searchPoint.get());
        List<User> users = userRepository.findUserWithinRadius(lat, lnt, 3);
        assertEquals(0, users.size());
    }

    @Test
    @Transactional
    public void shouldCreateUserWithASpecificServiceAreaAndFindByValidSubArea() throws Exception {
        Optional<ServiceArea> serviceAreaResult = OsmServiceAreaFactory.buildServiceArea(
                openStreetMapsClient, "Rio de Janeiro, Rio de Janeiro, Brasil", "Minha Quebrada");
        Optional<OpenStreetMapSearchResponse> searchPoint = OsmServiceAreaFactory.search(openStreetMapsClient, "Bangu, Rio de Janeiro, Brasil");

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        user.addServiceArea(serviceAreaResult.get());
        em.persist(user);

        SearchArea searchArea = new SearchArea(searchPoint.get().geojson().toJtsMultiPolygon());

        List<User> users = userRepository.findUserByServiceArea(searchArea);
        assertEquals(1, users.size());
    }

    @Test
    @Transactional
    public void shouldCreateUserOutsideServiceAreaAndNotFind() throws Exception {
        Optional<ServiceArea> serviceAreaResult = OsmServiceAreaFactory.buildServiceArea(
                openStreetMapsClient, "Barreiro, Portugal", "Example");
        Optional<OpenStreetMapSearchResponse> searchPoint = OsmServiceAreaFactory.search(openStreetMapsClient, "Almada, Portugal");

        if (serviceAreaResult.isEmpty() || searchPoint.isEmpty()) {
            throw new NoSuchElementException();
        }

        var user = new User();
        user.addServiceArea(serviceAreaResult.get());
        em.persist(user);

        SearchArea searchArea = new SearchArea(searchPoint.get().geojson().toJtsMultiPolygon());

        List<User> users = userRepository.findUserByServiceArea(searchArea);
        assertEquals(0, users.size());
    }
}
