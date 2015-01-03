package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.domain.Pong;
import service.domain.Stat;

import java.util.concurrent.TimeoutException;

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

    public void updateStats(Throwable e) {
        if (e instanceof TimeoutException) {
            logger.info("CSV error,timeout");

        } else {
            logger.info("CSV error," + e.getClass().getName() + "," + e.getMessage());
        }
    }

    public void updateStats(Pong pong) {
        checkNotNull(pong);

        Stat stat = new Stat(pong);
//        stats.add(stat);

        logger.info("CSV request sender," + stat.getRequestSender() + ",response sender," + stat.getResponseSender() + ",time from client to server," + stat.getTimeFromClientToServer() + ",time from server to client," + stat.getTimeFromServerToClient() + ",time on server," + stat.getTimeOnServer() + ",rountrip time," + stat.getTimeRoundtrip() + "," + stat.getRequestHeaders());
    }
}
