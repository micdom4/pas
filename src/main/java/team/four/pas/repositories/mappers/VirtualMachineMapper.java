package team.four.pas.repositories.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Mapper(uses = ObjectIdStringMapper.class)
public interface VirtualMachineMapper {

    VirtualMachine toData(VirtualMachineEntity entity);

    List<VirtualMachine> toDataList(List<VirtualMachineEntity> entities);

    VirtualMachineEntity toEntity(VirtualMachine data);

}
