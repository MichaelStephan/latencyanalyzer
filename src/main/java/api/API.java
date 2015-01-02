package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.StatsService;
import service.domain.Ping;
import service.domain.Pong;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
@Path("/latencyanalyzer")
public class API implements ExceptionMapper<Exception> {
    private final static Logger logger = LoggerFactory.getLogger(API.class);

    private StatsService statsService;

    public API(StatsService statsService) {
        this.statsService = checkNotNull(statsService);
    }

    @POST
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public void postStats(@Suspended final AsyncResponse asyncResponse, Ping ping) {
        checkNotNull(ping);

        synchronized (this) {
            try {
                this.wait(5000);
            } catch (Exception e) {
            }
        }

        Pong pong = new Pong(ping);
        asyncResponse.resume(Response.ok().entity(pong).build());
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public void getStats(@Suspended final AsyncResponse asyncResponse) {
        asyncResponse.resume(Response.ok().entity(statsService.getStats()).build());
    }

    @Override
    public Response toResponse(Exception e) {
        logger.error("an API call caused an exception", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

    }
}
