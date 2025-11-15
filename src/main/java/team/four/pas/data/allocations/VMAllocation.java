package team.four.pas.data.allocations;
import team.four.pas.data.resources.Resource;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Client;

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
