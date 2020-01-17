package ch.heg.ig.betRoyale.service;

import ch.heg.ig.betRoyale.api.dto.BlockChainDTO;
import ch.heg.ig.betRoyale.model.Block;
import ch.heg.ig.betRoyale.model.BlockChain;
import ch.heg.ig.betRoyale.model.Transaction;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Service class for the blochain
 */
@Service
public class BlockChainService {
    /**
     * Redis channel
     */
    public final static String BLOCKCHAIN_CHANNEL = "blockchain";

    private final static Logger logger = LoggerFactory.getLogger(BlockChainService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private volatile Queue<Transaction> currentTransactions;
    private volatile BlockChain chain;

    /**
     *
     * @param redisTemplate
     */
    @Autowired
    public BlockChainService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.chain = new BlockChain();
        this.currentTransactions = new ConcurrentLinkedQueue<>();
        //create the genesis block
        createBlock(Block.GENESIS_HASH);
    }


    /**
     * this method will create the block
     * he hash until the dificulty is respected
     * @param previousHash
     * @return
     */
    private Block createBlock(String previousHash) {
        int nonce = 0;
        int nextBlockId = this.chain.length() + 1;
        long timestamp = System.currentTimeMillis();
        String nextHash = this.chain.calculateHash(nextBlockId, previousHash, currentTransactions.toString(), timestamp, nonce);

        while (!this.chain.isDificultyValid(nextHash)) {
            nonce = nonce + 1;
            timestamp = System.currentTimeMillis();
            nextHash = this.chain.calculateHash(
                    nextBlockId,
                    previousHash,
                    currentTransactions.toString(),
                    timestamp,
                    nonce
            );
        }
        Block block = new Block(nextBlockId, previousHash, currentTransactions, timestamp, nonce);
        this.chain.add(block);
        this.currentTransactions = new ConcurrentLinkedQueue<>();
        return block;
    }

    /**
     * method will add a transaction in a block
     * @param transaction to add in the current block
     * @return number of block in the chain
     */
    public int createTransaction(Transaction transaction) {
        currentTransactions.add(transaction);
        return chain.lastBlock().getId() + 1;
    }


    /**
     * this method will mine a block
     * @return the new block mined
     */
    public Block mine() {
        Block newBlock = createBlock(this.chain.calculateHash(this.chain.lastBlock()));
        if (newBlock == null) {
            return null;
        }
        logger.info("A new block was created!");
        try {
            this.redisTemplate.convertAndSend(BLOCKCHAIN_CHANNEL, mapper.writeValueAsString(chain.getChain()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return newBlock;
    }

    /**
     *
     * @return
     */
    public BlockChainDTO chain() {
        return new BlockChainDTO(this.chain);
    }

    /**
     * This method is called by the Redis MessageListenerAdapter
     * control if the chain contained in the message is recent and valid
     * @param message
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void resolveChain(String message) throws JsonParseException, JsonMappingException, IOException {
        BlockChain otherChain = new BlockChain(mapper.readValue(message, new TypeReference<List<Block>>() {
        }));

        if (otherChain.isLongerThan(this.chain) && otherChain.isChainValid()) {
            this.chain = otherChain;
            logger.info("Our chain was replaced by others");
        }
    }

}
