package team.four.pas.repositories.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Mapper
public interface VMAllocationMapper {

    VMAllocation toData(VMAllocationEntity entity);

    List<VMAllocation> toDataList(List<VMAllocationEntity> entities);

    VMAllocationEntity toEntity(VMAllocation data);

    default String objectIdToString(ObjectId objectId) {
        if (objectId == null) {
            return null;
        }
        return objectId.toHexString();
    }

    default ObjectId stringToObjectId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return new ObjectId(id);
    }
}
