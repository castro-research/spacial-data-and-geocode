package org.acme.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;

@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager entityManager;

//    List<User> findUserServingPoint() {
        // Aqui eu vou buscar baseado na lat/lng
//    }

    public List<User> findUserWithinRadius(double lat, double lnt, int kilometers) {
        // Aqui eu procura quais usuários correspondem ao raio da minha localização
        // lat e lnt são a localização e kilometers é convertido para metros.
        double distanceInMeters = kilometers * 1000.0;
        Session session = entityManager.unwrap(Session.class);

        return session.createNativeQuery("""
                            SELECT DISTINCT u.*
                            FROM users u 
                            join service_areas sa 
                            ON sa.user_id = u.id
                            WHERE ST_DWithin(
                              sa.coverageArea::geography,
                              ST_SetSRID(
                                ST_MakePoint(:lnt, :lat), 4326
                              )::geography,
                              :radius
                            )
                        """, User.class)
                .addSynchronizedQuerySpace("users")
                .addSynchronizedQuerySpace("service_areas")
                .setParameter("lat", lat)
                .setParameter("lnt", lnt)
                .setParameter("radius", distanceInMeters)
                .getResultList();
    }

    private Point createPoint(double lat, double lnt) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(lnt, lat));
    }
}
