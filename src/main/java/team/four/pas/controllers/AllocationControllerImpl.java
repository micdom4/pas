package team.four.pas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.AllocationAddDTO;
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
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<List<VMAllocation>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getAll());
    }

    @GetMapping(
            {"/{id}"}
    )
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<VMAllocation> getById(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.findById(id));
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    @PreAuthorize(
            "(hasRole('ADMIN') && @ownershipChecker.isValidJws(#jws, #allocationAddDTO.clientId(), #allocationAddDTO.resourceId())) " +
                    "|| (hasRole('CLIENT') && @ownershipChecker.isOwner(authentication, #allocationAddDTO.clientId()))"
    )    public ResponseEntity<VMAllocation> createAllocation(@Valid @RequestBody AllocationAddDTO allocationAddDTO,
                                                         @RequestHeader(value = "X-Data-Integrity", required = false) String jws) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(allocationService.add(allocationAddDTO.clientId(), allocationAddDTO.resourceId(), Instant.now()));
    }

    @GetMapping(
            {"/past/vm/{id}"}
    )
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
            "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<List<VMAllocation>> getPastVmAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getPastVm(id));
    }

    @GetMapping(
            {"/active/vm/{id}"}
    )
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
            "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<List<VMAllocation>> getActiveVmAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getActiveVm(id));
    }

    @GetMapping(
            {"/active/client/{id}"}
    )
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)) " +
                    "|| " +
                    "(hasRole(T(team.four.pas.security.SecurityRoles).CLIENT) && @ownershipChecker.isOwner(authentication, #id))"
    )
    public ResponseEntity<List<VMAllocation>> getActiveClientAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getActiveClient(id));
    }

    @GetMapping(
            {"/past/client/{id}"}
    )
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)) " +
                    "|| " +
                    "(hasRole(T(team.four.pas.security.SecurityRoles).CLIENT) && @ownershipChecker.isOwner(authentication, #id))"
    )
    public ResponseEntity<List<VMAllocation>> getPastClientAllocations(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allocationService.getPastClient(id));
    }

    @PutMapping(
            {"/{id}/finish"}
    )
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)) " +
                    "|| " +
                    "(hasRole(T(team.four.pas.security.SecurityRoles).CLIENT) && @ownershipChecker.isOwnerOfAllocation(authentication, #id))"
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
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)) " +
                    "|| " +
                    "(hasRole(T(team.four.pas.security.SecurityRoles).CLIENT) && @ownershipChecker.isOwnerOfAllocation(authentication, #id))"
    )
    public ResponseEntity<?> deleteAllocation(@PathVariable String id) {
        allocationService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}