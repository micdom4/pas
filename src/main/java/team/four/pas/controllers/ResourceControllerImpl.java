package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.controllers.DTOs.mappers.ResourceToDTO;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.services.ResourceService;

import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = {"http://localhost:[*]"}
)
@RequestMapping(
        value = {"/resources"},
        produces = {"application/json"}
)
@RequiredArgsConstructor
public class ResourceControllerImpl {
    @NonNull
    private final ResourceService resourceService;
    @NonNull
    private final ResourceToDTO resourceToDTO;

    @GetMapping({""})
    public ResponseEntity<List<ResourceDTO>> getAll() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceToDTO.toDataList(resourceService.getAll()));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ResourceDTO> getResource(@PathVariable String id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceToDTO.toDTO(resourceService.findById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (ResourceIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<ResourceDTO> createVM(@RequestBody ResourceAddDTO vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(resourceToDTO.dtoFromVM(resourceService.addVM(resourceToDTO.vmFromAddDTO(vmDto))));
        } catch (ResourceDataException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<ResourceDTO> updateVM(@PathVariable String id, @RequestBody ResourceAddDTO vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceToDTO.toDTO(resourceService.updateVM(id, vmDto.cpuNumber(), vmDto.ramGiB(), vmDto.storageGiB())));
        } catch (ResourceIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (ResourceDataException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping(
            value = {"/{id}"}
    )
    public ResponseEntity<?> deleteVM(@PathVariable String id) {
        try {
            resourceService.deleteVM(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        } catch (ResourceIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (ResourceStillAllocatedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
