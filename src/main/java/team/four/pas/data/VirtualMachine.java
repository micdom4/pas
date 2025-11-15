package team.four.pas.data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VirtualMachine extends Resource {
    private int cpuNumber;
    private int ram;
    private int storage;

    public VirtualMachine(UUID id, int cpuNumber, int ram, int storage) {
        super(id);
        this.cpuNumber = cpuNumber;
        this.ram = ram;
        this.storage = storage;
    }

}
