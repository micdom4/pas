package team.four.pas.repositories;

import team.four.pas.data.resources.Resource;
import team.four.pas.data.resources.VirtualMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocalResourceRepository implements ResourceRepository {
    Map<UUID, Resource> map = new HashMap<>();

    @Override
    public Resource findById(UUID id) {
        return map.get(id);
    }

    @Override
    public List<Resource> findById(List<UUID> ids) {
        return ids.stream()
                .map(id -> map.get(id))
                .collect(Collectors.toList());
    }


    public boolean addVM(int cpuNumber, int ram, int memory) {
        VirtualMachine vm;

        do {
            vm = new VirtualMachine(UUID.randomUUID(), cpuNumber, ram, memory);
        } while(map.containsKey(vm.getId()));

        map.put(vm.getId(), vm);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        return map.remove(id) != null;
    }

}
