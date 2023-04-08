
package ru.installbiz.das_stroy.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ApproximateFirstReceiveTimestamp",
        "ApproximateReceiveCount",
        "SentTimestamp"
})
public class Attributes {

    @JsonProperty("ApproximateFirstReceiveTimestamp")
    private String approximateFirstReceiveTimestamp;
    @JsonProperty("ApproximateReceiveCount")
    private String approximateReceiveCount;
    @JsonProperty("SentTimestamp")
    private String sentTimestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("ApproximateFirstReceiveTimestamp")
    public String getApproximateFirstReceiveTimestamp() {
        return approximateFirstReceiveTimestamp;
    }

    @JsonProperty("ApproximateFirstReceiveTimestamp")
    public void setApproximateFirstReceiveTimestamp(String approximateFirstReceiveTimestamp) {
        this.approximateFirstReceiveTimestamp = approximateFirstReceiveTimestamp;
    }

    @JsonProperty("ApproximateReceiveCount")
    public String getApproximateReceiveCount() {
        return approximateReceiveCount;
    }

    @JsonProperty("ApproximateReceiveCount")
    public void setApproximateReceiveCount(String approximateReceiveCount) {
        this.approximateReceiveCount = approximateReceiveCount;
    }

    @JsonProperty("SentTimestamp")
    public String getSentTimestamp() {
        return sentTimestamp;
    }

    @JsonProperty("SentTimestamp")
    public void setSentTimestamp(String sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
