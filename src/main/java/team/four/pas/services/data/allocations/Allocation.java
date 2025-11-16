package team.four.pas.services.data.allocations;

import lombok.Getter;
import team.four.pas.services.data.IdentifiableObject;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Getter
public abstract class Allocation extends IdentifiableObject {
    private final Client client;
    private final Instant startTime;
    private Instant endTime;

    public abstract Resource getResource();

    public Allocation(String id, Client client, Instant startTime) {
        super(id);
        this.client = client;
        this.startTime = startTime;
    }

    public String getId() {
        return super.getId();
    }

    public void finishAllocation() {
        this.endTime = Instant.now();
    }

}
