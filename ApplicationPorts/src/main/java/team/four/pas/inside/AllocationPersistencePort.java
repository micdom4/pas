package team.four.pas.inside;

import team.four.pas.services.data.allocations.VMAllocation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AllocationPersistencePort {
    VMAllocation insert(VMAllocation vmAllocation);
    void deleteById(String id);
    Optional<VMAllocation> findById(String id);
    List<VMAllocation> findAll();
    List<VMAllocation> findByVmIdAndEndTimeIsNull(String vmId);
    List<VMAllocation> findByVmIdAndEndTimeIsNotNull(String vmId);

    List<VMAllocation> findByClientIdAndEndTimeIsNull(String clientId);
    List<VMAllocation> findByClientIdAndEndTimeIsNotNull(String clientId);

    void finishAllocation(String allocationId, Instant endTime);
}
