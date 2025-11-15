package team.four.pas.data.allocations;

import team.four.pas.data.IdentifiableEntity;

import java.util.UUID;

public abstract class Allocation extends IdentifiableEntity {

    public Allocation(UUID uuid) {
        super(uuid);
    }
}
