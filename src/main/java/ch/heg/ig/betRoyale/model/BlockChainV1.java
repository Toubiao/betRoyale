package ch.heg.ig.betRoyale.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BlockChainV1 {

    public static final int DIFFICULTY_NUMBER = 5;
    public static final String POW_PREFIX = new String(new char[DIFFICULTY_NUMBER]).replace('\0', '0');
    private final String id = UUID.randomUUID().toString();

    private final Deque<BlockV1> chain;

    public BlockChainV1() {
        this.chain = new ConcurrentLinkedDeque<>();
    }

    public BlockChainV1(List<BlockV1> chain) {
        this.chain = new ConcurrentLinkedDeque<>(chain);
    }

    public boolean add(BlockV1 block, String id) {
        if (! this.id.equals(id)) {
            return false;
        }
        this.chain.add(block);
        return true;
    }

    public List<BlockV1> getChain() {
        return new ArrayList<>(chain);
    }

    public int length() {
        return this.chain.size();
    }

    public String id() {
        return this.id;
    }

    public BlockV1 lastBlock() {
        return chain.getLast();
    }

    public int proofOfWork() {
        return proofOfWork(lastBlock().getNonce());
    }

    private int proofOfWork(int lastNonce) {
        int nonce = 0;
        while (!isNonceValid(lastNonce, nonce)) {
            nonce++;
        }
        return nonce;
    }

    private boolean isNonceValid(int lastNonce, int nonce) {
        String s = lastNonce + "" + nonce;
        return DigestUtils.sha256Hex(s).startsWith(POW_PREFIX);
    }

    public boolean isLongerThan(BlockChainV1 another) {
        return this.length() > another.length();
    }

    public boolean isChainValid() {
        if (chain.isEmpty()) {
            return false;
        }
        Iterator<BlockV1> iter = chain.iterator();
        BlockV1 last = iter.next();
        while (iter.hasNext()) {
            BlockV1 curr = iter.next();
            if (! last.hash().equals(curr.getPreviousHash())) {
                return false;
            }
            if (! isNonceValid(last.getNonce(), curr.getNonce())) {
                return false;
            }
            last = curr;
        }
        return true;
    }

}
