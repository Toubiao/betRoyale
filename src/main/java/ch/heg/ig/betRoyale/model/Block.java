package ch.heg.ig.betRoyale.model;

import ch.heg.ig.betRoyale.handler.EncryptionUtils;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

public class Block {
    public static final String GENESIS_HASH = "1";

    public String hash;

    @JsonProperty
    public String previousHash;
    @JsonProperty
    private List<Transaction> transactions;; //our data will be a simple message.
    @JsonProperty
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    @JsonProperty
    private int nonce;

    //Block Constructor.

    public Block(String data, String previousHash ) {
        this.transactions = List<Transaction> transactions;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        String calculatedhash = EncryptionUtils.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        transactions.toString()
        );
        return calculatedhash;
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //
    public String getData() {
        return data;
    }

    //TO DO
    //Communication entre Pc ?

}
