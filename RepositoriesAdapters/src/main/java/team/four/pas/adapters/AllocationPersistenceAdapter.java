package team.four.pas.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.four.pas.mappers.AllocationMapper;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.inside.AllocationPersistencePort;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AllocationPersistenceAdapter implements AllocationPersistencePort {
    private final AllocationRepository repository;
    private final AllocationMapper mapper;


    @Override
    public VMAllocation insert(VMAllocation vmAllocation) {
        return mapper.entityToDomain(
                    repository.insert(mapper.domainToEntity(vmAllocation))
                );
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<VMAllocation> findById(String id) {
        return Optional.ofNullable(mapper.entityToDomain(repository.findById(id).orElse(null)));
    }

    @Override
    public List<VMAllocation> findAll() {
        return mapper.entityToDomain(repository.findAll());
    }

    @Override
    public List<VMAllocation> findByVmIdAndEndTimeIsNull(String vmId) {
        return mapper.entityToDomain(repository.findByVmIdAndEndTimeIsNull(vmId));
    }

    @Override
    public List<VMAllocation> findByVmIdAndEndTimeIsNotNull(String vmId) {
        return mapper.entityToDomain(repository.findByVmIdAndEndTimeIsNotNull(vmId));
    }

    @Override
    public List<VMAllocation> findByClientIdAndEndTimeIsNull(String clientId) {
        return mapper.entityToDomain(repository.findByClientIdAndEndTimeIsNull(clientId));
    }

    @Override
    public List<VMAllocation> findByClientIdAndEndTimeIsNotNull(String clientId) {
        return mapper.entityToDomain(repository.findByClientIdAndEndTimeIsNotNull(clientId));
    }

    @Override
    public void finishAllocation(String allocationId, Instant endTime) {
        repository.finishAllocation(allocationId, endTime);
    }
}
