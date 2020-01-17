package ch.heg.ig.betRoyale.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Object that represent a block used in the blockchain structure
 * We can find many implementation accross the web
 */
public class Block {

    /**
     * Constant used for the genesis block (first block in the chain)
     */
    public static final String GENESIS_HASH = "0";



    /**
     * the id is the number of block in the chain
     */
    @JsonProperty
    private int id;
    /**
     *  hash of the previous block is used for verification of the blockchain
     */
    @JsonProperty
    public String previousHash;
    /**
     * List of transaction in the block
     */
    @JsonProperty
    private List<Transaction> transactions;

    @JsonProperty
    private long timeStamp;
    /**
     * This is used for the proof of work
     * we increment this variable until the hash begin by 00000
     * it's depend of the dificulty used
     */
    @JsonProperty
    private int nonce;

    public Block() {
    }

    /**
     * Constructor
     * @param id the id is the number of block in the chain
     * @param previousHash hash of the previous block is used for verification of the blockchain
     * @param transactions List of transaction in the block
     * @param timeStamp time when the block have been mined
     * @param nonce used for the proof of work
     */
    public Block(int id, String previousHash, Queue<Transaction> transactions, Long timeStamp, int nonce) {
        this.id = id;
        this.nonce = nonce;
        this.timeStamp = timeStamp;
        if (transactions != null) {
            this.transactions = new ArrayList<>(transactions);
        } else {
            this.transactions = new ArrayList<>();
        }
        this.previousHash = previousHash;
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
