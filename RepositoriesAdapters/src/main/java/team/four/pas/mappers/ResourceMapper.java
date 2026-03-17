package team.four.pas.mappers;

import org.mapstruct.Mapper;
import team.four.pas.entities.resources.VirtualMachineEntity;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    VirtualMachine entityToDomain(VirtualMachineEntity vm);
    List<VirtualMachine> entityToDomain(List<VirtualMachineEntity> vm);
    VirtualMachineEntity domainToEntity(VirtualMachine vm);
}
