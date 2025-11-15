package team.four.pas.repositories;

import team.four.pas.data.allocations.Allocation;
import team.four.pas.data.resources.Resource;
import team.four.pas.data.users.User;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocalAllocationRepository implements AllocationRepository {
    Map<UUID, Allocation> allocations;

    @Override
    public Allocation findById(UUID id) {
        return allocations.get(id);
    }

    @Override
    public List<Allocation> findById(List<UUID> ids) {
        return ids.stream()
                  .map(id -> allocations.get(id))
                  .collect(Collectors.toList());
    }

    public List<Allocation> getActive(User user) {
        return allocations.values()
                          .stream()
                          .filter(allocation -> allocation.getClient().equals(user))
                          .filter(allocation -> allocation.getEndTime() == null)
                          .collect(Collectors.toList());
    }

    public List<Allocation> getActive(Resource resource) {
        return allocations.values()
                          .stream()
                          .filter(allocation -> allocation.getResource().equals(resource))
                          .filter(allocation -> allocation.getEndTime() == null)
                          .collect(Collectors.toList());
    }

    public List<Allocation> getPast(User user) {
        return allocations.values()
                          .stream()
                          .filter(allocation -> allocation.getClient().equals(user))
                          .filter(allocation -> allocation.getEndTime() != null)
                          .collect(Collectors.toList());
    }

    public List<Allocation> getPast(Resource resource) {
        return allocations.values()
                          .stream()
                          .filter(allocation -> allocation.getResource().equals(resource))
                          .filter(allocation -> allocation.getEndTime() != null)
                          .collect(Collectors.toList());
    }

    /*
    public boolean addAllocation(UUID clientId, UUID resourceId, Instant startTime) {
    }
     */

    public boolean finishAllocation(UUID id) {
        allocations.get(id).finishAllocation();
        return true;
    }
    @Override
    public boolean delete(UUID id) {
        return allocations.remove(id) != null;
    }

}
