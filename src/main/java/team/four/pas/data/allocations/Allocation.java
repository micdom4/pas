package team.four.pas.data.allocations;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import team.four.pas.data.IdentifiableEntity;
import team.four.pas.data.resources.Resource;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.User;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Allocation extends IdentifiableEntity {
    private final Client client;
    private final Instant startTime;
    private Instant endTime;

    public abstract Resource getResource();

    public Allocation(UUID uuid, Client client, Instant startTime) {
        super(uuid);
        this.client = client;
        this.startTime = startTime;
    }

    public UUID getId() {
        return super.getId();
    }

    public void finishAllocation() {
        this.endTime = Instant.now();
    }

}
