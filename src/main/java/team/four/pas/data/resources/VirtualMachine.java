package team.four.pas.data.resources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class VirtualMachine extends Resource {
    private int cpuNumber;
    private int ramGiB;
    private int storageGiB;

    public VirtualMachine(UUID id, int cpuNumber, int ramGiB, int storageGiB) {
        super(id);
        this.cpuNumber = cpuNumber;
        this.ramGiB = ramGiB;
        this.storageGiB = storageGiB;
    }

}
