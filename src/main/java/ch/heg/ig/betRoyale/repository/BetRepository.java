package ch.heg.ig.betRoyale.repository;

import ch.heg.ig.betRoyale.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO used to store and access the Bet object in the db
 */
@Repository
public interface BetRepository extends MongoRepository<Bet,String> {

}
