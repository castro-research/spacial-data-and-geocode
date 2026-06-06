package org.acme.client.openstreetmaps;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.acme.client.openstreetmaps.responses.OpenStreetMapSearchResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(baseUri = "https://nominatim.openstreetmap.org")
public interface OpenStreetMapsClient {
    @GET
    @Path("/search")
    List<OpenStreetMapSearchResponse> search(
        @QueryParam("q") String query,
        @QueryParam("format") String format,
        @QueryParam("polygon_geojson") int polygonJson
    );
}
