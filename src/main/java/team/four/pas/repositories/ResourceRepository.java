package team.four.pas.repositories;

import team.four.pas.services.data.resources.VirtualMachine;

import java.util.UUID;


public interface ResourceRepository extends Repository<VirtualMachine> {
    boolean addVM(int cpuNumber, int ram, int memory);
    public boolean updateVM(UUID id, int cpuNumber, int ram, int memory);
}

