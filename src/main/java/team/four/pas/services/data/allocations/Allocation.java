package team.four.pas.services.data.allocations;

import lombok.Getter;
import team.four.pas.services.data.IdentifiableObject;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Allocation extends IdentifiableObject {
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
