package team.four.pas.inside;

import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;
import java.util.Optional;

public interface ResourcePersistencePort {
    List<VirtualMachine> findAll();

    Optional<VirtualMachine> findById(String id);

    VirtualMachine insert(VirtualMachine vm);

    void updateById(String id, int cpuNumber, int ramGiB, int storageGiB);
}
