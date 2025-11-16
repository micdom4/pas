package team.four.pas.services.data.allocations;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.UUID;

public class VMAllocation extends Allocation {
    private final VirtualMachine vm;

    public VMAllocation(UUID uuid, Client client, VirtualMachine vm, Instant startTime) {
        super(uuid, client, startTime);
        this.vm = vm;
    }

    @Override
    public Resource getResource() {
        return vm;
    }
}
