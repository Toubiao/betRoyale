package ch.heg.ig.betRoyale.repository;

import ch.heg.ig.betRoyale.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    User findUserByPseudoEquals(String pseudo);
}
