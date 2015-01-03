package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by i303874 on 1/2/15.
 */
public class Measurable {
    private long sendTime;

    private long receiveTime;

    public String toString() {
        return
                "sendTime: " + sendTime + "\n" +
                        "receiveTime: " + receiveTime;
    }

    public Measurable() {
        this.sendTime = System.currentTimeMillis();
    }

    public Measurable(Measurable measurable) {
        this(measurable.getSendTime());
    }

    @JsonCreator
    public Measurable(@JsonProperty("sendTime") long sendTime) {
        this.sendTime = sendTime;
        this.receiveTime = System.currentTimeMillis();
    }

    public long getSendTime() {
        return sendTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }
}
