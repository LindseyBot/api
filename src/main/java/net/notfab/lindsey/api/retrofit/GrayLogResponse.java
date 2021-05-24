package net.notfab.lindsey.api.retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrayLogResponse {

    private List<AuditMessage> messages;

    @JsonProperty("total_results")
    private int results;

}
