package team.four.pas.data.users;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class User {
    @NonNull private UUID id;
    @NonNull private String login;
    @NonNull private String name;
    @NonNull private String surname;
    @Setter private boolean active;
}
