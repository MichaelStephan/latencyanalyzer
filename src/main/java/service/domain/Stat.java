package service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Stat {
    @JsonIgnore
    private long timestamp;

    @JsonIgnore
    private Pong pong;

    public Stat(Pong pong) {
        this.pong = checkNotNull(pong);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimeFromClientToServer() {
        return pong.getRequest().getReceiveTime() - pong.getRequest().getSendTime();
    }

    public Map<String, Object> getResponseSenderProperties() {
        return pong.getResponseSenderProperties();
    }

    public Map<String, Object> getRequestSenderProperties() {
        return pong.getRequestSenderProperties();
    }

    public long getTimeFromServerToClient() {
        return pong.getResponse().getReceiveTime() - pong.getResponse().getSendTime();
    }

    public long getTimeRoundtrip() {
        return pong.getResponse().getReceiveTime() - pong.getRequest().getSendTime();
    }

    public long getTimeOnServer() {
        return pong.getResponse().getSendTime() - pong.getRequest().getReceiveTime();
    }
}
