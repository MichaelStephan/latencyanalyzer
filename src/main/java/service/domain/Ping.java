package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Ping {
    private Map<String, Object> senderProperties;

    private Measurable request;

    public Ping(Map<String, Object> senderProperties) {
        this.request = new Measurable();
        this.senderProperties = checkNotNull(senderProperties);
    }

    @JsonCreator
    public Ping(@JsonProperty("senderProperties") Map<String, Object> senderProperties, @JsonProperty("request") Measurable request) {
        this.senderProperties = checkNotNull(senderProperties);
        this.request = new Measurable(checkNotNull(request));
    }

    public Measurable getRequest() {
        return request;
    }

    public Map<String, Object> getSenderProperties() {
        return senderProperties;
    }

}
