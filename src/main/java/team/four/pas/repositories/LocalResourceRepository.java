package team.four.pas.repositories;
import team.four.pas.data.resources.Resource;
import team.four.pas.data.resources.VirtualMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocalResourceRepository implements ResourceRepository {
    Map<UUID, Resource> resources = new HashMap<>();

    @Override
    public List<Resource> getAll() {
        return resources.values().stream().collect(Collectors.toList());
    }

    @Override
    public Resource findById(UUID id) {
        return resources.get(id);
    }

    @Override
    public List<Resource> findById(List<UUID> ids) {
        return ids.stream()
                .map(id -> resources.get(id))
                .collect(Collectors.toList());
    }


    public boolean addVM(int cpuNumber, int ram, int memory) {
        VirtualMachine vm;

        do {
            vm = new VirtualMachine(UUID.randomUUID(), cpuNumber, ram, memory);
        } while(resources.containsKey(vm.getId()));

        resources.put(vm.getId(), vm);
        return true;
    }

    @Override
    public boolean updateVM(UUID id, int cpuNumber, int ram, int memory) {
        VirtualMachine vm = (VirtualMachine) resources.get(id);
        vm.setCpuNumber(cpuNumber);
        vm.setRamGiB(ram);
        vm.setStorageGiB(memory);

        return true;
    }

    @Override
    public boolean delete(UUID id) {
        return resources.remove(id) != null;
    }

}
