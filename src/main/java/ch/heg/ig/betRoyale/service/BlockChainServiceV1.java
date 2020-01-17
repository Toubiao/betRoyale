package ch.heg.ig.betRoyale.service;

import ch.heg.ig.betRoyale.api.dto.BlockChainDTOV1;
import ch.heg.ig.betRoyale.model.Block;
import ch.heg.ig.betRoyale.model.BlockChainV1;
import ch.heg.ig.betRoyale.model.BlockV1;
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
 *
 */
@Service
public class BlockChainServiceV1 {
    /**
     *
     */
    public final static String BLOCKCHAIN_CHANNEL = "blockchain";
    private final static Logger logger = LoggerFactory.getLogger(BlockChainServiceV1.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private volatile Queue<Transaction> currentTransactions;
    private volatile BlockChainV1 chain;

    @Autowired
    public BlockChainServiceV1(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.chain = new BlockChainV1();
        //create the genesis block
        createBlock(Block.GENESIS_HASH, BlockV1.GENESIS_Nonce);
    }


    private BlockV1 createBlock(String previousHash, int nonce) {
        String id = this.chain.id();
        BlockV1 block = new BlockV1(this.chain.length() + 1,previousHash,currentTransactions,nonce);
        boolean ok = this.chain.add(block, id);
        if (! ok) {
            return null;
        }
        this.currentTransactions = new ConcurrentLinkedQueue<>();
        return block;
    }

    public int createTransaction(Transaction transaction) {
        currentTransactions.add(transaction);
        return chain.lastBlock().getId() + 1;
    }

    public BlockV1 mine() {
        int nonce = chain.proofOfWork();
        BlockV1 newBlock = createBlock(chain.lastBlock().hash(), nonce);
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

    public BlockChainDTOV1 chain() {
        return new BlockChainDTOV1(this.chain);
    }

    public void resolveChain(String message) throws JsonParseException, JsonMappingException, IOException {
        BlockChainV1 otherChain = new BlockChainV1(mapper.readValue(message, new TypeReference<List<BlockV1>>() {}));

        if (otherChain.isLongerThan(this.chain) && otherChain.isChainValid()) {
            this.chain = otherChain;
            logger.info("Our chain was replaced by others");
        }
    }

}
