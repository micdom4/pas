package team.four.pas.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import team.four.pas.model.users.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByLogin(String login);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'password' : ?1 } }")
    void updatePasswordById(String id, String newEncodedPassword);

    List<User> findByLoginStartingWith(String login);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'surname' : ?1 } }")
    void updateSurnameById(String id, String surname);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'active' : ?1 } }")
    void updateActiveById(String id, boolean active);
}
