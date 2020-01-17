package ch.heg.ig.betRoyale.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


public class BlockChain {

    public static final int DIFFICULTY_NUMBER = 5;
    public static final String DIFFICULTY_PREFIX = new String(new char[DIFFICULTY_NUMBER]).replace('\0', '0');


    private final Deque<Block> chain;

    public BlockChain() {
        this.chain = new ConcurrentLinkedDeque<>();
    }

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


    public boolean isDificultyValid(String hash) {
        return hash.substring( 0, DIFFICULTY_NUMBER).equals(DIFFICULTY_PREFIX);
    }

    public boolean isLongerThan(BlockChain another) {
        return this.length() > another.length();
    }

    public String calculateHash(Block block) {
        return calculateHash(block.getId(),block.getPreviousHash(), block.getTransactions().toString(),block.getTimeStamp(),block.getNonce());
    }

    public String calculateHash(int id, String previousHash, String transactions, long timeStamp, int nonce) {
        return DigestUtils.sha256Hex(id + "|" + timeStamp + "|" + transactions
                + "|" + nonce + "|" + previousHash);
    }

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
