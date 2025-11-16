package team.four.pas.repositories.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Mapper
public interface VirtualMachineMapper {

    VirtualMachine toData(VirtualMachineEntity entity);

    List<VirtualMachine> toDataList(List<VirtualMachineEntity> entities);

    VirtualMachineEntity toEntity(VirtualMachine data);

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
