package team.four.pas.data;

import java.util.UUID;

public abstract class Resource extends IdentifiableEntity {
    public Resource(UUID uuid) {
        super(uuid);
    }
}
