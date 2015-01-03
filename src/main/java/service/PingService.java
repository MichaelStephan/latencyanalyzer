package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.domain.Ping;
import service.domain.Pong;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class PingService {
    private final static int INITIAL_DELAY = 5;

    private final static Logger logger = LoggerFactory.getLogger(PingService.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PingService(StatsService statsService, int interval, URL url) {
        checkNotNull(statsService);
        checkArgument(interval >= 5);
        checkNotNull(url);

        scheduler.scheduleAtFixedRate(() -> {
            WebTarget target = ClientBuilder.newClient().target(url.getProtocol() + "://" + url.getHost() + ":" + url.getPort()).path(url.getPath());

            target.request(MediaType.APPLICATION_JSON).async().post(Entity.entity(new Ping(), MediaType.APPLICATION_JSON), new InvocationCallback<Response>() {
                @Override
                public void completed(Response response) {
                    if (response.getStatus() == 200) {
                        try {
                            statsService.updateStats(response.readEntity(Pong.class));
                        } catch (Exception e) {
                            logger.warn("ping wasn't answered with pong object", e);
                        }
                    } else {
                        logger.warn("ping was answered with invalid response code " + response.getStatus() + " (" + response.getStatusInfo().getReasonPhrase() + ") by " + url.toString());
                    }
                }

                @Override
                public void failed(Throwable throwable) {
                    logger.warn("ping failed", throwable);
                }
            });
        }, INITIAL_DELAY, interval, TimeUnit.SECONDS);
    }
}