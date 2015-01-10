package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.StatsService;
import service.domain.Ping;
import service.domain.Pong;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
@Path("/latencyanalyzer")
public class API implements ExceptionMapper<Exception> {
    private final static Logger logger = LoggerFactory.getLogger(API.class);

    private Map<String, Object> properties;

    private StatsService statsService;

    public API(Map<String, Object> properties, StatsService statsService) {
        this.properties = checkNotNull(properties);
        this.statsService = checkNotNull(statsService);
    }

    @POST
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public void postStats(@Suspended final AsyncResponse asyncResponse, @Context HttpHeaders headers, Ping ping) {
        checkNotNull(ping);

        Map<String, Object> receiverProperties = new HashMap<String, Object>();
        properties.entrySet().forEach((entry) -> {
            receiverProperties.put(entry.getKey(), entry.getValue());
        });
        receiverProperties.put("headers", headers.getRequestHeaders());

        Pong pong = new Pong(receiverProperties, ping);
        asyncResponse.resume(Response.ok().entity(pong).build());
    }

//    @GET
//    @Path("/stat/{type}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void getStats(@Suspended final AsyncResponse asyncResponse, @PathParam("type") String statType) {
//        asyncResponse.resume(Response.ok().entity(statsService.getStats(statType)).build());
//    }

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof NoSuchElementException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            logger.error("an API call caused an exception", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
