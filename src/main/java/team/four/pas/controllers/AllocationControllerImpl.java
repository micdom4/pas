package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceFindException;
import team.four.pas.exceptions.user.UserFindException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/allocations"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AllocationControllerImpl {
    private final @NonNull AllocationService allocationService;

    @GetMapping({""})
    public List<VMAllocation> getAll() {
        return allocationService.getAll();
    }

    @PostMapping(value = {""}, consumes = {"application/json"})
    public ResponseEntity<VMAllocation> createAllocation(@RequestBody UserDTO userDTO, VirtualMachine vm) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(allocationService.add(userDTO, vm, Instant.now()));
        } catch (AllocationClientException | AllocationResourceException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (InactiveClientException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (ResourceAlreadyAllocatedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping({"/past/vm/{id}"})
    public ResponseEntity<List<VMAllocation>> getPastVmAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getPastVm(id));
        } catch (ResourceFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AllocationResourceException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping({"/active/vm/{id}"})
    public ResponseEntity<List<VMAllocation>> getActiveVmAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getActiveVm(id));
        } catch (ResourceFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AllocationResourceException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping({"/active/client/{id}"})
    public ResponseEntity<List<VMAllocation>> getActiveClientAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getActiveClient(id));
        } catch (UserFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AllocationClientException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping({"/past/client/{id}"})
    public ResponseEntity<List<VMAllocation>> getPastClientAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getPastClient(id));
        } catch (UserFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AllocationClientException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping({"/{id}/finish"})
    public ResponseEntity<?> finishAllocation(@PathVariable String id) {
        try {
            allocationService.finishAllocation(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AllocationIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (AllocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping({"/{id}"})
    public void deleteAllocation(@PathVariable String id) {
        allocationService.delete(id);
    }
}