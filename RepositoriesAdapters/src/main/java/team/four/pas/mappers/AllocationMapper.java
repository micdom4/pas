package team.four.pas.mappers;

import org.mapstruct.Mapper;
import team.four.pas.entities.allocations.VMAllocationEntity;
import team.four.pas.services.data.allocations.VMAllocation;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AllocationMapper {

    VMAllocation entityToDomain(VMAllocationEntity vmAllocation);
    Optional<VMAllocation> entityToDomain(Optional<VMAllocationEntity> vmAllocation);
    List<VMAllocation> entityToDomain(List<VMAllocationEntity> vmAllocation);

    VMAllocationEntity domainToEntity(VMAllocation vmAllocation);
}
