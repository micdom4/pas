package team.four.pas.data.users;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public abstract class User {
    private UUID id;
    private String login;
    private String name;
    private String surname;
    @Setter private boolean active;
}
