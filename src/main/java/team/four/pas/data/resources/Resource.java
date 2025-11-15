package team.four.pas.data.resources;

import team.four.pas.data.IdentifiableEntity;

import java.util.UUID;

public abstract class Resource extends IdentifiableEntity {
    public Resource(UUID uuid) {
        super(uuid);
    }
}
