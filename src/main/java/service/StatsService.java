package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.domain.ErrorStat;
import service.domain.Ping;
import service.domain.Pong;
import service.domain.SuccessStat;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class StatsService {
    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Logger logger = LoggerFactory.getLogger(StatsService.class);

    public void updateStats(Ping ping, Throwable e, long timeUntilError) {
        checkNotNull(ping);
        checkNotNull(e);
        checkNotNull(timeUntilError);

        ErrorStat errorStat = new ErrorStat(ping, e, timeUntilError);

        try {
            logger.info("STAT ERROR " + objectMapper.writeValueAsString(errorStat));
        } catch (Exception e1) {
            logger.warn("unable to convert status to json", e1);
        }
    }

    public void updateStats(Pong pong) {
        checkNotNull(pong);

        SuccessStat successStat = new SuccessStat(pong);

        try {
            logger.info("STAT SUCCESS " + objectMapper.writeValueAsString(successStat));
        } catch (Exception e1) {
            logger.warn("unable to convert status to json", e1);
        }
    }
}
