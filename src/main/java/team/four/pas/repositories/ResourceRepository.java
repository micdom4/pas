package team.four.pas.repositories;

import team.four.pas.services.data.resources.VirtualMachine;

public interface ResourceRepository extends Repository<VirtualMachine> {
    boolean addVM(int cpuNumber, int ram, int memory);
    public boolean updateVM(String id, int cpuNumber, int ram, int memory);
    public VirtualMachine findById(String id);
    public boolean delete(String id);
}

