package ch.heg.ig.betRoyale;

import ch.heg.ig.betRoyale.service.BlockChainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class BetRoyaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetRoyaleApplication.class, args);
	}

	@Bean
	public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
												   MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		container.addMessageListener(listenerAdapter, new PatternTopic(BlockChainService.BLOCKCHAIN_CHANNEL));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(BlockChainService blockChainService) {
		return new MessageListenerAdapter(blockChainService, "resolveChain");
	}

}
