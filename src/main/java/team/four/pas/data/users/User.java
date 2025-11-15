package team.four.pas.data.users;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class User {
    @NonNull private UUID id;
    @NonNull private String login;
    @NonNull private String name;
    @Setter @NonNull private String surname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().append(id, user.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
