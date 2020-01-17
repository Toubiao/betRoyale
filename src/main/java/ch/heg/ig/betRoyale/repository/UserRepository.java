package ch.heg.ig.betRoyale.repository;

import ch.heg.ig.betRoyale.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO used to store and access the User object in the db
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    User findUserByPseudoEquals(String pseudo);
}
