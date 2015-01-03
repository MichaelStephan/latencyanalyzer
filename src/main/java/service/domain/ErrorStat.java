package service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class ErrorStat {
    @JsonIgnore
    private Ping ping;

    @JsonIgnore
    private Throwable error;

    private long timeUntilError;

    public ErrorStat(Ping ping, Throwable error, long timeUntilError) {
        this.ping = checkNotNull(ping);
        this.error = checkNotNull(error);
        this.timeUntilError = checkNotNull(timeUntilError);
    }

    public long getTimeUntilError() {
        return timeUntilError;
    }

    public Map<String, Object> getSenderProperties() {
        return ping.getSenderProperties();
    }

    public String getErrorType() {
        return error.getClass().getName();
    }

    public String getErrorMessage() {
        return error.getMessage();
    }

    public Ping getPing() {
        return ping;
    }
}
