package team.four.pas.mappers;

import org.mapstruct.Mapper;
import team.four.pas.entities.allocations.VMAllocationEntity;
import team.four.pas.services.data.allocations.VMAllocation;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AllocationMapper {

    VMAllocation entityToDomain(VMAllocationEntity vmAllocation);
    List<VMAllocation> entityToDomain(List<VMAllocationEntity> vmAllocation);

    VMAllocationEntity domainToEntity(VMAllocation vmAllocation);
}
