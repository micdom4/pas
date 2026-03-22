package team.four.pas.DTO.mappers;

import org.mapstruct.Mapper;
import team.four.pas.DTO.ResourceAddDTO;
import team.four.pas.DTO.ResourceDTO;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ResourceToDTO {

    ResourceDTO dtoFromVM(VirtualMachine vm);

    VirtualMachine vmFromDTO(ResourceDTO resourceDTO);

    VirtualMachine vmFromAddDTO(ResourceAddDTO resourceDTO);

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
