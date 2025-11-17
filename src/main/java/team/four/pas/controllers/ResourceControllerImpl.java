package team.four.pas.controllers;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.exceptions.service.*;
import team.four.pas.services.ResourceService;
import team.four.pas.services.data.resources.VirtualMachine;

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
public class ResourceControllerImpl implements ResourceController {
    @NonNull
    private final ResourceService resourceService;

    @GetMapping({""})
    public List<VirtualMachine> getAll() {
        return resourceService.getAll();
    }

    @GetMapping({"/{id}"})
    public VirtualMachine getResource(@PathVariable String id) {
        return resourceService.findById(id);
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> createVM(@RequestBody VmDto vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(resourceService.addVM(vmDto.getCpus(), vmDto.getRam(), vmDto.getMemory()));

        } catch (AddVMException avme) {
            if (avme.getCause().getClass() == DataValidationException.class) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> updateVM(@PathVariable String id, @RequestBody VmDto vmDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resourceService.updateVM(id, vmDto.getCpus(), vmDto.getRam(), vmDto.getMemory()));
        } catch (UpdateVMException dve) {
            if (dve.getCause().getClass() == DataValidationException.class) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(null);
            }
        } catch (Exception e) {
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
        } catch (DeleteVMException dvme) {
            if (dvme.getCause().getClass() == ResourceStillAllocatedException.class) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}

@Data
class VmDto {
    private int cpus;
    private int ram;
    private int memory;
}
