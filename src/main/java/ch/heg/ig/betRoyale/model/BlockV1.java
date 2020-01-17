package ch.heg.ig.betRoyale.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BlockV1 {
    public static final String GENESIS_HASH = "0";
    public static final int GENESIS_Nonce = 100;


    @JsonProperty
    private int id;
    @JsonProperty
    public String previousHash;
    @JsonProperty
    private List<Transaction> transactions;; //our data will be a simple message.
    @JsonProperty
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    @JsonProperty
    private int nonce;

    //Block Constructor.

    public BlockV1(int id, String previousHash, Queue<Transaction> transactions, int nonce) {
        this.id = id;
        this.nonce = nonce;
        this.timeStamp = System.currentTimeMillis();
        if (transactions != null) {
            this.transactions = new ArrayList<>(transactions);
        } else {
            this.transactions = new ArrayList<>();
        }
        this.previousHash = previousHash;
    }

    //Calculate new hash based on blocks contents
    public String hash() {
        return DigestUtils.sha256Hex(id + "|" + timeStamp + "|" + transactions.toString()
                + "|" + nonce + "|" + previousHash);
    }

    public int getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getNonce() {
        return nonce;
    }
}
