package team.four.pas.repositories;

import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.resources.VirtualMachine;

import java.util.UUID;


interface ResourceRepository extends Repository<VirtualMachine> {
    boolean addVM(int cpuNumber, int ram, int memory);
    public boolean updateVM(UUID id, int cpuNumber, int ram, int memory);
}

