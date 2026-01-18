package team.four.pas.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import team.four.pas.repositories.entities.TokenEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<TokenEntity, String> {

    @Query("{ 'userLogin': ?0, $or: [ { 'expired': false }, { 'revoked': false } ] }")
    List<TokenEntity> findFirstByUserLogin(String userLogin);

    Optional<TokenEntity> findFirstByToken(String token);
}