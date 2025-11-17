package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = {"http://localhost:[*]"}
)
@RequestMapping(
        value = {"/allocations"},
        produces = {"application/json"}
)
@RequiredArgsConstructor
public class AllocationControllerImpl {
    private final @NonNull AllocationService allocationService;

    @GetMapping({""})
    public List<VMAllocation> getAll() {
        return allocationService.getAll();
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<VMAllocation> createAllocation(@RequestBody UserDTO userDTO, VirtualMachine vm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(allocationService.add(userDTO, vm, Instant.now()));
    }

    @GetMapping({"/past/vm/{id}"})
    public List<VMAllocation> getPastVmAllocations(@PathVariable String id) {
        return allocationService.getPastVm(id);
    }

    @GetMapping({"/active/vm/{id}"})
    public List<VMAllocation> getActiveVmAllocations(@PathVariable String id) {
        return allocationService.getActiveVm(id);
    }

    @GetMapping({"/active/client/{id}"})
    public List<VMAllocation> getActiveClientAllocations(@PathVariable String id) {
        return allocationService.getActiveClient(id);
    }

    @GetMapping({"/past/client/{id}"})
    public List<VMAllocation> getPastClientAllocations(@PathVariable String id) {
        return allocationService.getPastClient(id);
    }

    @PostMapping({"/{id}/finish"})
    public void finishAllocation(@PathVariable String id) {
        allocationService.finishAllocation(id);
    }

    @DeleteMapping({"/{id}"})
    public void deleteAllocation(@PathVariable String id) {
        allocationService.delete(id);
    }
}