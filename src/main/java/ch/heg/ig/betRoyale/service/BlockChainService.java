package ch.heg.ig.betRoyale.service;

import ch.heg.ig.betRoyale.model.BlockChain;
import ch.heg.ig.betRoyale.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service

public class BlockChainService {
    /**
     *
     */
    public final static String BLOCKCHAIN_CHANNEL = "blockchain";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private volatile Queue<Transaction> currentTransactions;
    private volatile BlockChain chain;

}
