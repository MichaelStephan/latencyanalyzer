package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.domain.Ping;
import service.domain.Pong;
import service.domain.Stat;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class StatsService {
    private final static Logger logger = LoggerFactory.getLogger(StatsService.class);

//    private Stats stats = new Stats();

//    public Stats getStats() {
//        return stats;
//    }

    public void updateStats(Ping ping, Throwable e, long timeUntilError) {
        logger.info("CSV2 error|time until error|" + timeUntilError + "|" + e.getClass().getName() + "|" + e.getMessage()+"|"+ping.getSenderProperties());
    }

    public void updateStats(Pong pong) {
        checkNotNull(pong);

        Stat stat = new Stat(pong);
//        stats.add(stat);

        Map<String, Object> requestSenderProperties = stat.getRequestSenderProperties();
        Map<String, Object> responseSenderProperties = stat.getResponseSenderProperties();

        logger.info("CSV1 request sender|" + requestSenderProperties.get("sender") + "|response sender|" + responseSenderProperties.get("sender") + "|time from client to server|" + stat.getTimeFromClientToServer() + "|time from server to client|" + stat.getTimeFromServerToClient() + "|time on server|" + stat.getTimeOnServer() + "|rountrip time|" + stat.getTimeRoundtrip() + "|request sender properties|" + requestSenderProperties + "|response sender properties|" + responseSenderProperties);
    }
}
