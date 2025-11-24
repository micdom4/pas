package team.four.pas.services.mappers;

import org.mapstruct.Mapper;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ResourceToDTO {

    ResourceDTO dtoFromVM(VirtualMachine vm);

    VirtualMachine vmFromDTO(ResourceDTO resourceDTO);

    default List<ResourceDTO> toDataList(List<VirtualMachine> vms) {
        if (vms == null) {
            return null;
        }
        return vms.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    default ResourceDTO toDTO(VirtualMachine vm) {
        if (vm == null) {
            return null;
        }

        return dtoFromVM(vm);
    }
}
