package team.four.pas.repositories.entities;


import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import java.util.UUID;

@Getter @Setter
public class UserEntity {

    @BsonId
    private UUID id;
    private String login;
    private String name;
    private String surname;
}
