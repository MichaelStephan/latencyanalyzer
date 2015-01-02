package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Measurable {
    private String correlationId;

    private long sendTime;

    private long receiveTime;

    public String toString() {
        return
                "correlationId:  " + correlationId + "\n" +
                        "sendTime: " + sendTime + "\n" +
                        "receiveTime: " + receiveTime;
    }

    public Measurable() {
        this.correlationId = UUID.randomUUID().toString();
        this.sendTime = System.currentTimeMillis();
    }

    public Measurable(Measurable measurable) {
        this(measurable.getCorrelationId(), measurable.getSendTime());
    }

    @JsonCreator
    public Measurable(@JsonProperty("correlationId") String correlationId, @JsonProperty("sendTime") long sendTime) {
        this.correlationId = checkNotNull(correlationId);
        this.sendTime = sendTime;
        this.receiveTime = System.currentTimeMillis();
    }

    public long getSendTime() {
        return sendTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
