package team.four.pas.services.data.resources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
public class VirtualMachine extends Resource {
    private int cpuNumber;
    private int ramGiB;
    private int storageGiB;

    public VirtualMachine(String id, int cpuNumber, int ramGiB, int storageGiB) {
        super(id);
        this.cpuNumber = cpuNumber;
        this.ramGiB = ramGiB;
        this.storageGiB = storageGiB;
    }

    @Override
    public String toString() {
        return super.toString() + "VirtualMachine{" +
                "cpuNumber=" + cpuNumber +
                ", ramGiB=" + ramGiB +
                ", storageGiB=" + storageGiB +
                '}';
    }
}
