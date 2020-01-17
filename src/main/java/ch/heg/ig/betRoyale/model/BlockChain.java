package ch.heg.ig.betRoyale.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Object used for store the blocks
 */
public class BlockChain {

    /**
     * Constant used for the proof of work.
     * In this case the hash of a block need to start with five zero (00000)
     */
    public static final int DIFFICULTY_NUMBER = 5;

    /**
     * Constant used for testing the validity of the hash
     * is equals 00000
     */
    public static final String DIFFICULTY_PREFIX = new String(new char[DIFFICULTY_NUMBER]).replace('\0', '0');


    /**
     * The Collections used for the storage of the blocks
     * the type is questionable
     *
     */
    private final Deque<Block> chain;

    public BlockChain() {
        this.chain = new ConcurrentLinkedDeque<>();
    }

    /**
     * Constructor
     * @param chain take a blockchain
     */
    public BlockChain(List<Block> chain) {
        this.chain = new ConcurrentLinkedDeque<>(chain);
    }

    public boolean add(Block block) {
        this.chain.add(block);
        return true;
    }

    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }

    public int length() {
        return this.chain.size();
    }


    public Block lastBlock() {
        return chain.getLast();
    }


    /**
     * method will test the validity of the hash
     * @param hash block hash tested
     * @return the result of the validation operation
     */
    public boolean isDificultyValid(String hash) {
        return hash.substring( 0, DIFFICULTY_NUMBER).equals(DIFFICULTY_PREFIX);
    }

    /**
     * method when the blockchain is replicated across nodes
     * @param another
     * @return
     */
    public boolean isLongerThan(BlockChain another) {
        return this.length() > another.length();
    }

    public String calculateHash(Block block) {
        return calculateHash(block.getId(),block.getPreviousHash(), block.getTransactions().toString(),block.getTimeStamp(),block.getNonce());
    }

    /**
     * Method used for hash a block
     * @param id the id is the number of block in the chain
     * @param previousHash hash of the previous block is used for verification of the blockchain
     * @param transactions List of transaction in the block
     * @param timeStamp
     * @param nonce POW
     * @return
     */
    public String calculateHash(int id, String previousHash, String transactions, long timeStamp, int nonce) {
        return DigestUtils.sha256Hex(id + "|" + timeStamp + "|" + transactions
                + "|" + nonce + "|" + previousHash);
    }

    /**
     * method used for the verification of the entire blockchain
     * @return
     */
    public boolean isChainValid() {
        if (chain.isEmpty()) {
            return false;
        }
        Iterator<Block> iter = chain.iterator();
        Block last = iter.next();
        while (iter.hasNext()) {
            Block curr = iter.next();
            if (!calculateHash(last).equals(curr.getPreviousHash())) {
                return false;
            }
            if (!isDificultyValid(calculateHash(curr))) {
                return false;
            }
            last = curr;
        }
        return true;
    }

}
