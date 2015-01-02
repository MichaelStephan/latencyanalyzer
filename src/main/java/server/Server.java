package server;

import api.API;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.PingService;
import service.StatsService;

import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by i303874 on 12/23/14.
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private int port;

    public Server(int port) {
        checkArgument(port > 0 && port <= 65535);
        this.port = port;
    }

    public void run() throws ServerException {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS | ServletContextHandler.NO_SECURITY);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new ServletContainer(resourceConfig())), "/*");

        try {
            logger.info("running webserver on port " + port);
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("execution of webserver failed", e);
            throw new ServerException(e);
        } finally {
            if (server != null) {
                server.destroy();
            }
        }
    }

    private ResourceConfig resourceConfig() {
        try {
            ResourceConfig resourceConfig = new ResourceConfig();

            ObjectMapper om = new ObjectMapper();
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
            provider.setMapper(om);
            resourceConfig.register(provider);

            StatsService statsService = new StatsService();
            PingService pingService = new PingService(statsService, 5, new URL("http://localhost:9999/latencyanalyzer/ping"));
            resourceConfig.register(new API(statsService));

            return resourceConfig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
