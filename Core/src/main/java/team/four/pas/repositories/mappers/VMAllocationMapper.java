package team.four.pas.repositories.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.services.data.allocations.VMAllocation;

import java.util.List;

@Mapper (uses = {
        StringToObjectId.class,
        UserMapper.class,
        VirtualMachineMapper.class
})
public interface VMAllocationMapper {

    VMAllocation toData(VMAllocationEntity entity);

    List<VMAllocation> toDataList(List<VMAllocationEntity> entities);

    VMAllocationEntity toEntity(VMAllocation data);
}
