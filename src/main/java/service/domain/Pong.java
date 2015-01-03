package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Pong {
    private Map<String, Object> requestSenderProperties;

    private Map<String, Object> responseSenderProperties;

    private Measurable request;

    private Measurable response;

    public Pong(Map<String, Object> responseSenderProperties, Ping ping) {
        checkNotNull(ping);
        this.requestSenderProperties = checkNotNull(ping.getSenderProperties());
        this.responseSenderProperties = checkNotNull(responseSenderProperties);
        this.request = checkNotNull(ping.getRequest());
        this.response = new Measurable();
    }

    @JsonCreator
    public Pong(@JsonProperty("requestSenderProperties") Map<String, Object> requestSenderProperties, @JsonProperty("responseSenderProperties") Map<String, Object> responseSenderProperties, @JsonProperty("request") Measurable request, @JsonProperty("response") Measurable response) {
        this.requestSenderProperties = checkNotNull(requestSenderProperties);
        this.responseSenderProperties = checkNotNull(responseSenderProperties);
        this.request = checkNotNull(request);
        this.response = new Measurable(checkNotNull(response));
    }

    public String toString() {
        return "request: " + request + "\n" +
                "response: " + response;
    }

    public Map<String, Object> getResponseSenderProperties() {
        return responseSenderProperties;
    }

    public Map<String, Object> getRequestSenderProperties() {
        return requestSenderProperties;
    }

    public Measurable getRequest() {
        return request;
    }

    public Measurable getResponse() {
        return response;
    }
}
