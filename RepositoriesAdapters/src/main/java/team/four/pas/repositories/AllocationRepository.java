package team.four.pas.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import team.four.pas.entities.allocations.VMAllocationEntity;

import java.time.Instant;
import java.util.List;

public interface AllocationRepository extends MongoRepository<VMAllocationEntity, String> {

    List<VMAllocationEntity> findByVmIdAndEndTimeIsNull(String vmId);
    List<VMAllocationEntity> findByVmIdAndEndTimeIsNotNull(String vmId);

    List<VMAllocationEntity> findByClientIdAndEndTimeIsNull(String clientId);
    List<VMAllocationEntity> findByClientIdAndEndTimeIsNotNull(String clientId);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'endTime' : ?1 } }")
    void finishAllocation(String allocationId, Instant endTime);
}
