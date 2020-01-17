package ch.heg.ig.betRoyale.api.dto;

import ch.heg.ig.betRoyale.model.BlockChain;
import ch.heg.ig.betRoyale.model.Block;

import java.util.List;

public class BlockChainDTO {
    private int length;
    private List<Block> chain;

    public BlockChainDTO(BlockChain chain) {
        this.chain = chain.getChain();
        this.length = chain.length();
    }

    public int getLength() {
        return this.length;
    }

    public List<Block> getChain() {
        return this.chain;
    }
}
