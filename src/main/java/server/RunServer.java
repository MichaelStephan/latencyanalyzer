package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        Server server = new Server(webServerPort);
        try {
            server.run();
        } catch (ServerException e) {
            logger.error("saying good bye due to error", e);
        }
    }
}
