package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Ping {
    private Measurable request;

    public Ping() {
        this.request = new Measurable();
    }

    @JsonCreator
    public Ping(@JsonProperty("request") Measurable request) {
        this.request = new Measurable(checkNotNull(request));
    }

    public Measurable getRequest() {
        return request;
    }
}
