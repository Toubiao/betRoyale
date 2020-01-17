package ch.heg.ig.betRoyale.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *Object used in a Block
 * This is the "data" of the block
 * in our case we store the money transfer
 */
public class Transaction {

    @JsonProperty
    private String sender;
    @JsonProperty
    private String recipient;
    @JsonProperty
    private double amount;

    public Transaction() {}

    public Transaction(String sender, String recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String toString() {
        return "sender:" + sender + ", recipient:" + recipient + ", amount:" + amount;
    }
}
