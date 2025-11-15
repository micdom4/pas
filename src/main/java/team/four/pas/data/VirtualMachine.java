package team.four.pas.data;
import lombok.Getter;

@Getter
public class VirtualMachine extends Resource {
    private int cpuNumber;
    private int ram;
    private int storage;
}
