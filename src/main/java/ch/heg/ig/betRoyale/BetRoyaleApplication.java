package ch.heg.ig.betRoyale;

import ch.heg.ig.betRoyale.model.Bet;
import ch.heg.ig.betRoyale.model.User;
import ch.heg.ig.betRoyale.repository.BetRepository;
import ch.heg.ig.betRoyale.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BetRoyaleApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	@Autowired
	BetRepository betRepository;

	public static void main(String[] args) {
		SpringApplication.run(BetRoyaleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		userRepository.deleteAll();
		betRepository.deleteAll();

		List<User> users = new ArrayList<>(5);
		users.add(new User("Oracle",""));
		users.add(new User("Ouss","G6HJ87"));
		users.add(new User("ArabMitGeld","B4GH6L"));
		users.add(new User("ArabOhneGeld","Q6MNL7"));
		users.add(new User("Marvin","A2BG5R"));
		userRepository.saveAll(users);

	}
}
