package team.four.pas.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.AllocationAddDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.security.SecurityRoles;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/allocations"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AllocationControllerImpl {
    private final AllocationService allocationService;

    @GetMapping(
            {""}
    )
    @PreAuthorize("hasRole(T(team.four.pas.security.SecurityRoles).ADMIN)")
    public ResponseEntity<List<VMAllocation>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getAll());
    }

    @GetMapping(
            {"/{id}"}
    )
    public ResponseEntity<VMAllocation> getById(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.findById(id));
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<VMAllocation> createAllocation(@Valid @RequestBody AllocationAddDTO allocationAddDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(allocationService.add(allocationAddDTO.clientId(), allocationAddDTO.resourceId(), Instant.now()));
    }

    @GetMapping(
            {"/past/vm/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getPastVmAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getPastVm(id));
    }

    @GetMapping(
            {"/active/vm/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getActiveVmAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getActiveVm(id));
    }

    @GetMapping(
            {"/active/client/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getActiveClientAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getActiveClient(id));
    }

    @GetMapping(
            {"/past/client/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getPastClientAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getPastClient(id));
    }

    @PutMapping(
            {"/{id}/finish"}
    )
    public ResponseEntity<?> finishAllocation(@PathVariable String id) {
        allocationService.finishAllocation(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(
            {"/{id}"}
    )
    public ResponseEntity<?> deleteAllocation(@PathVariable String id) {
        allocationService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}