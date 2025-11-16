package team.four.pas.services.data.allocations;
import lombok.Getter;
import lombok.ToString;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.UUID;

@Getter
public class VMAllocation extends Allocation {
    private final VirtualMachine vm;

    public VMAllocation(String id, Client client, VirtualMachine vm, Instant startTime) {
        super(id, client, startTime);
        this.vm = vm;
    }

    @Override
    public Resource getResource() {
        return vm;
    }

    @Override
    public String toString() {
        return super.toString() + "VMAllocation{" +
                "vm=" + vm +
                '}';
    }
}
