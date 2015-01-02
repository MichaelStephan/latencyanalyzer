package service.domain;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Stat {
    private long timestamp;

    private long timeFromClientToServer;

    private long timeFromServerToClient;

    private long timeOnServer;

    private long timeRoundtrip;

    public Stat(Pong pong) {
        checkNotNull(pong);
        this.timeFromClientToServer = System.currentTimeMillis();
        this.timeFromClientToServer = pong.getRequest().getReceiveTime() - pong.getRequest().getSendTime();
        this.timeFromServerToClient = pong.getResponse().getReceiveTime() - pong.getResponse().getSendTime();
        this.timeOnServer = pong.getResponse().getSendTime() - pong.getRequest().getReceiveTime();
        this.timeRoundtrip = pong.getResponse().getReceiveTime() - pong.getRequest().getSendTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimeFromClientToServer() {
        return timeFromClientToServer;
    }

    public long getTimeFromServerToClient() {
        return timeFromServerToClient;
    }

    public long getTimeRoundtrip() {
        return timeRoundtrip;
    }

    public long getTimeOnServer() {
        return timeOnServer;
    }
}
