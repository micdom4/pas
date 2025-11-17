package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DataValidationException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
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
public class ResourceControllerImpl {
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
    public ResponseEntity<?> createVM(@RequestBody int cpus, @RequestBody int ram, @RequestBody int memory) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.addVM(cpus, ram, memory));

        } catch (AddVMException avme) {
            if (avme.getCause().getClass() == DataValidationException.class) {
                return ResponseEntity
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
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
    public ResponseEntity<?> updateVM(@PathVariable String id, @RequestBody int cpus, @RequestBody int ram, @RequestBody int memory) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resourceService.updateVM(id, cpus, ram, memory));
        } catch (UpdateVMException dvme) {
            if (dvme.getCause().getClass() == DataValidationException.class) {
                return ResponseEntity
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
