package team.four.pas.services.data.resources;

import lombok.ToString;
import team.four.pas.services.data.IdentifiableObject;

@ToString
public abstract class Resource extends IdentifiableObject {
    public Resource(String id) {
        super(id);
    }
}
