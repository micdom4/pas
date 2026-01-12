package team.four.pas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.controllers.DTOs.mappers.ResourceToDTO;
import team.four.pas.services.ResourceService;

import java.util.List;

@RestController
@RequestMapping(
        value = {"/resources"},
        produces = {"application/json"}
)
@RequiredArgsConstructor
public class ResourceControllerImpl {
    private final ResourceService resourceService;
    private final ResourceToDTO resourceToDTO;

    @GetMapping({""})
    public ResponseEntity<List<ResourceDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resourceToDTO.toDataList(resourceService.getAll()));

    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ResourceDTO> getResource(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resourceToDTO.toDTO(resourceService.findById(id)));
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<ResourceDTO> createVM(@Valid @RequestBody ResourceAddDTO vmDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resourceToDTO.dtoFromVM(resourceService.addVM(resourceToDTO.vmFromAddDTO(vmDto))));
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<ResourceDTO> updateVM(@PathVariable String id, @Valid @RequestBody ResourceAddDTO vmDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resourceToDTO.toDTO(resourceService.updateVM(id, vmDto.cpuNumber(), vmDto.ramGiB(), vmDto.storageGiB())));
    }

    @DeleteMapping(
            value = {"/{id}"}
    )
    public ResponseEntity<?> deleteVM(@PathVariable String id) {
        resourceService.deleteVM(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
