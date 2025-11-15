package team.four.pas.data.resources;

import lombok.ToString;
import team.four.pas.data.IdentifiableEntity;

import java.util.UUID;

@ToString
public abstract class Resource extends IdentifiableEntity {
    public Resource(UUID uuid) {
        super(uuid);
    }
}
