package net.notfab.lindsey.api.models;

import lombok.Data;

@Data
public class VoteSummary {

    private long up;
    private long down;
    private Boolean voted;

}
