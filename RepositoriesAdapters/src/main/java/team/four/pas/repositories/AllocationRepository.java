package team.four.pas.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import team.four.pas.model.allocations.VMAllocation;

import java.time.Instant;
import java.util.List;

public interface AllocationRepository extends MongoRepository<VMAllocation, String> {

    List<VMAllocation> findByVmIdAndEndTimeIsNull(String vmId);
    List<VMAllocation> findByVmIdAndEndTimeIsNotNull(String vmId);

    List<VMAllocation> findByClientIdAndEndTimeIsNull(String clientId);
    List<VMAllocation> findByClientIdAndEndTimeIsNotNull(String clientId);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'endTime' : ?1 } }")
    void finishAllocation(String allocationId, Instant endTime);
}
