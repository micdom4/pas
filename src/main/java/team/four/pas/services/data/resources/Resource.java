package team.four.pas.services.data.resources;

import lombok.ToString;
import team.four.pas.services.data.IdentifiableObject;

import java.util.UUID;

@ToString
public abstract class Resource extends IdentifiableObject {
    public Resource(UUID uuid) {
        super(uuid);
    }
}
