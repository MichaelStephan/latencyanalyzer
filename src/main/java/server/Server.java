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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 12/23/14.
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private Map<String, Object> properties;

    private String serverId = UUID.randomUUID().toString();

    private int port;

    private URL url;

    private int interval;

    public Server(int port, URL url, int interval, Map<String, Object> properties) {
        checkArgument(port > 0 && port <= 65535);
        checkArgument(interval > 0);
        checkNotNull(url);

        this.port = port;
        this.url = url;
        this.interval = interval;
        this.properties = checkNotNull(properties);

        properties.put("sender", serverId);
        properties.put("senderIps", "unknown");

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            String senderIps = "";
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    senderIps += i.getHostAddress() + " ";
                }
            }
            properties.put("senderIps", senderIps);
        } catch (Exception e) {
        }
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
            new PingService(properties, statsService, interval, url);
            resourceConfig.register(new API(properties, statsService));

            return resourceConfig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
