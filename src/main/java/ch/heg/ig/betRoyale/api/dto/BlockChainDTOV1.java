package ch.heg.ig.betRoyale.api.dto;

import ch.heg.ig.betRoyale.model.BlockChainV1;
import ch.heg.ig.betRoyale.model.BlockV1;

import java.util.List;

/**
 * Same as the other version
 */
public class BlockChainDTOV1 {
    private int length;
    private List<BlockV1> chain;

    public BlockChainDTOV1(BlockChainV1 chain) {
        this.chain = chain.getChain();
        this.length = chain.length();
    }

    public int getLength() {
        return this.length;
    }

    public List<BlockV1> getChain() {
        return this.chain;
    }
}
