package org.acme.support;

import org.acme.client.openstreetmaps.OpenStreetMapsClient;
import org.acme.client.openstreetmaps.responses.OpenStreetMapSearchResponse;
import org.acme.servicearea.ServiceArea;

import java.util.Optional;

public final class OsmServiceAreaFactory {

    private OsmServiceAreaFactory() {
    }

    public static Optional<OpenStreetMapSearchResponse> search(OpenStreetMapsClient client, String address) {
        return client.search(address, "json", 1)
                .stream()
                .filter(r -> r.geojson() != null && r.geojson().isArea())
                .findFirst();
    }

    public static Optional<ServiceArea> buildServiceArea(OpenStreetMapsClient client, String searchAddress, String serviceAreaAddress) {
        return search(client, searchAddress).map(result -> {
            var serviceArea = new ServiceArea();
            serviceArea.setAddress(serviceAreaAddress);
            serviceArea.setCoverageArea(result.geojson().toJtsMultiPolygon());
            return serviceArea;
        });
    }

    public static Optional<ServiceArea> buildServiceArea(OpenStreetMapsClient client, String address) {
        return buildServiceArea(client, address, address);
    }

    public static double parseLat(OpenStreetMapSearchResponse response) {
        return Double.parseDouble(response.lat());
    }

    public static double parseLon(OpenStreetMapSearchResponse response) {
        return Double.parseDouble(response.lon());
    }
}
