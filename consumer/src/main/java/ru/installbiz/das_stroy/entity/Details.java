
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
        "queue_id",
        "message"
})
public class Details {

    @JsonProperty("queue_id")
    private String queueId;
    @JsonProperty("message")
    private Message__1 message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("queue_id")
    public String getQueueId() {
        return queueId;
    }

    @JsonProperty("queue_id")
    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    @JsonProperty("message")
    public Message__1 getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Message__1 message) {
        this.message = message;
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
