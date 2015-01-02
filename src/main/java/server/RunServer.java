package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by i303874 on 12/31/14.
 */
public class RunServer {
    private final static Logger logger = LoggerFactory.getLogger(RunServer.class);

    private final static int ERRORCODE = 200;

    private final static void bye(Exception e) {
        logger.error("saying good bye due to error", e);
        System.exit(ERRORCODE);
    }

    private final static URL getURL() throws Exception {
        String url = null;
        try {
            url = System.getenv("URL");
            if (url.trim().equals("")) {
                throw new Exception("environment variable URL is empty");
            }
        } catch (NullPointerException e) {
            throw new Exception("environment variable URL is not set");
        }

        return new URL(url);
    }

    private final static int getInterval() throws Exception {
        String intervalStr = null;
        try {
            intervalStr = System.getenv("INTERVAL");
            if (intervalStr.trim().equals("")) {
                throw new Exception("environment variable INTERVAL is empty");
            }
        } catch (NullPointerException e) {
            throw new Exception("environment variable INTERVAL is not set");
        }

        try {
            return Integer.parseInt(intervalStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("environment variable INTERVAL could not be parse into integer");
        }
    }

    private final static int getPort() throws Exception {
        String portStr = null;
        try {
            portStr = System.getenv("PORT");
            if (portStr.trim().equals("")) {
                throw new Exception("environment variable PORT is empty");
            }
        } catch (NullPointerException e) {
            throw new Exception("environment variable PORT is not set");
        }

        try {
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("environment variable PORT could not be parse into integer");
        }
    }

    public static void main(String[] args) {
        int webServerPort = -1;
        try {
            webServerPort = getPort();
        } catch (Exception e) {
            bye(e);
        }

        int interval = -1;
        try {
            interval = getInterval();
        } catch (Exception e) {
            bye(e);
        }

        URL url = null;
        try {
            url = getURL();
        } catch (Exception e) {
            bye(e);
        }

        Server server = new Server(webServerPort, url, interval);
        try {
            server.run();
        } catch (ServerException e) {
            logger.error("saying good bye due to error", e);
        }
    }
}
