package ch.heg.ig.betRoyale.configuration;

import ch.heg.ig.betRoyale.service.BlockChainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Class used for the redis configuration.
 * Redis is used to send the blockchain to the others node when a node mine a new block.
 *
 */
@Configuration
public class RedisConfiguration {

    /**
     * String-focused extension of RedisTemplate. Since most operations against Redis are String based, this class provides a dedicated class that minimizes configuration of its more generic template especially in terms of serializers.
     * @param connectionFactory factory of Redis connections
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    /**
     * Container providing asynchronous behaviour for Redis message listeners. Handles the low level details of listening, converting and message dispatching.
     * As oppose to the low level Redis (one connection per subscription), the container uses only one connection that is 'multiplexed' for all registered listeners, the message dispatch being done through the task executor
     * chanel used to share the blockchain across the nodes
     * @param connectionFactory factory of Redis connections
     * @param listenerAdapter Message listener adapter that delegates the handling of messages to target listener methods via reflection
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(BlockChainService.BLOCKCHAIN_CHANNEL));

        return container;
    }


    /**
     * Message listener adapter that delegates the handling of messages to target listener methods via reflection
     * @param blockChainService Object used for handling the message. The method called for handling a new message is BlockChainService.resolveChain
     * @return MessageListenerAdapter
     */
    @Bean
    MessageListenerAdapter listenerAdapter(BlockChainService blockChainService) {
        return new MessageListenerAdapter(blockChainService, "resolveChain");
    }

}
