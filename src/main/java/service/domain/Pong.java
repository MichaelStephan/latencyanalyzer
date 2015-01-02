package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Pong {
    private Measurable request;

    private Measurable response;

    public Pong(Ping ping) {
        checkNotNull(ping);
        this.request = checkNotNull(ping.getRequest());
        this.response = new Measurable();
    }

    @JsonCreator
    public Pong(@JsonProperty("request") Measurable request, @JsonProperty("response") Measurable response) {
        this.request = checkNotNull(request);
        this.response = new Measurable(checkNotNull(response));
    }

    public String toString() {
        return "request: " + request + "\n" +
                "response: " + response;
    }

    public Measurable getRequest() {
        return request;
    }

    public Measurable getResponse() {
        return response;
    }
}
