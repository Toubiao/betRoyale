package ch.heg.ig.betRoyale.repository;

import ch.heg.ig.betRoyale.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetRepository extends MongoRepository<Bet,String> {

}
