package team.four.pas.services.data.allocations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import team.four.pas.services.data.IdentifiableObject;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Getter
@ToString
public abstract class Allocation extends IdentifiableObject {
    private final Client client;
    private final Instant startTime;
    private final Instant endTime;

    public abstract Resource getResource();

    public Allocation(String id, Client client, Instant startTime, Instant endTime) {
        super(id);
        this.client = client;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return super.getId();
    }

}
