package service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Pong {
    private String requestSender;

    private Map<String, List<String>> requestHeaders;

    private String responseSender;

    private Measurable request;

    private Measurable response;

    public Pong(String serverId, Map<String, List<String>> requestHeaders, Ping ping) {
        checkNotNull(ping);
        this.requestHeaders = checkNotNull(requestHeaders);
        this.requestSender = checkNotNull(ping.getSender());
        this.responseSender = checkNotNull(serverId);
        this.request = checkNotNull(ping.getRequest());
        this.response = new Measurable();
    }

    @JsonCreator
    public Pong(@JsonProperty("requestSender") String requestSender, @JsonProperty("requestHeaders") Map<String, List<String>> requestHeaders, @JsonProperty("responseSender") String responseSender, @JsonProperty("request") Measurable request, @JsonProperty("response") Measurable response) {
        this.requestSender = checkNotNull(requestSender);
        this.responseSender = checkNotNull(responseSender);
        this.request = checkNotNull(request);
        this.requestHeaders = checkNotNull(requestHeaders);
        this.response = new Measurable(checkNotNull(response));
    }

    public String toString() {
        return "request: " + request + "\n" +
                "response: " + response;
    }

    public String getRequestSender() {
        return requestSender;
    }

    public String getResponseSender() {
        return responseSender;
    }

    public Map<String, List<String>> getRequestHeaders() {
        return requestHeaders;
    }

    public Measurable getRequest() {
        return request;
    }

    public Measurable getResponse() {
        return response;
    }
}
