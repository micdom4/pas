package team.four.pas.services;

import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAll();

    ResourceDTO findById(String id) throws ResourceIdException, ResourceNotFoundException;

    ResourceDTO addVM(ResourceAddDTO vmDTO) throws ResourceDataException;

    ResourceDTO updateVM(String id, ResourceAddDTO vmDTO) throws ResourceIdException, ResourceNotFoundException, ResourceDataException;

    void deleteVM(String id) throws ResourceIdException, ResourceNotFoundException, ResourceStillAllocatedException;
}
