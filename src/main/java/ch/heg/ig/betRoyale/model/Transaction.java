package ch.heg.ig.betRoyale.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    @JsonProperty
    private String sender;
    @JsonProperty
    private String recipient;
    @JsonProperty
    private int amount;
}
