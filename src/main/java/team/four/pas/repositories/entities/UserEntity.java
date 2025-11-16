package team.four.pas.repositories.entities;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;


public record UserEntity(
        @BsonId ObjectId id,
        @BsonProperty("login") String login,
        @BsonProperty("name") String name,
        @BsonProperty("surname") String surname,
        @BsonProperty("type") Type type,
        @BsonProperty("active") boolean active
)  {

    public enum Type {
        CLIENT,
        MANAGER,
        ADMIN
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return new EqualsBuilder().append(id, that.id).isEquals();
    }

}