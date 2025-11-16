package team.four.pas.services.data.resources;

import lombok.ToString;
import team.four.pas.services.data.IdentifiableEntity;

import java.util.UUID;

@ToString
public abstract class Resource extends IdentifiableEntity {
    public Resource(UUID uuid) {
        super(uuid);
    }
}
