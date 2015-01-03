package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Ping {
    private String sender;

    private Measurable request;

    public Ping(String sender) {
        this.request = new Measurable();
        this.sender = checkNotNull(sender);
    }

    @JsonCreator
    public Ping(@JsonProperty("sender") String sender, @JsonProperty("request") Measurable request) {
        this.sender = checkNotNull(sender);
        this.request = new Measurable(checkNotNull(request));
    }

    public String getSender() {
        return sender;
    }

    public Measurable getRequest() {
        return request;
    }
}
