package team.four.pas.services.data.allocations;
import lombok.Getter;
import lombok.Setter;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Getter
public class VMAllocation extends Allocation {
    private final VirtualMachine vm;

    public VMAllocation(String id, Client client, VirtualMachine vm, Instant startTime, Instant endTime) {
        super(id, client, startTime, endTime);
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
