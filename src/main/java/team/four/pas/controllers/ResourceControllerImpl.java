package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.exceptions.resource.*;
import team.four.pas.services.ResourceService;

@RestController
@CrossOrigin(
        originPatterns = {"http://localhost:[*]"}
)
@RequestMapping(
        value = {"/resources"},
        produces = {"application/json"}
)
@RequiredArgsConstructor
public class ResourceControllerImpl implements ResourceController {
    @NonNull
    private final ResourceService resourceService;

    @GetMapping({""})
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceService.getAll());
        } catch (ResourceGetAllException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getResource(@PathVariable String id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceService.findById(id));
        } catch (ResourceFindException ex) {
            if (ex.getCause() instanceof ResourceNotFoundException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> createVM(@RequestBody ResourceDTO vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(resourceService.addVM(vmDto.cpus(), vmDto.ram(), vmDto.memory()));
        } catch (ResourceAddException rae) {
            if (rae.getCause() instanceof ResourceDataException) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> updateVM(@PathVariable String id, @RequestBody ResourceDTO vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceService.updateVM(id, vmDto.cpus(), vmDto.ram(), vmDto.memory()));
        } catch (ResourceUpdateException dve) {
            if (dve.getCause() instanceof ResourceDataException) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else if (dve.getCause() instanceof ResourceNotFoundException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
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
        } catch (ResourceDeleteException rde) {
            if (rde.getCause() instanceof ResourceStillAllocatedException) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(null);
            } else if (rde.getCause() instanceof ResourceNotFoundException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }
}
