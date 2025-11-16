package team.four.pas.repositories.entities;


import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;


@Getter @Setter
public class UserEntity extends IdentifiableEntity {

    @BsonProperty
    private String login;

    @BsonProperty
    private String name;

    @BsonProperty
    private String surname;
}
