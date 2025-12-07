package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.AllocationAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.mappers.ResourceToDTO;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.user.UserIdException;
import team.four.pas.exceptions.user.UserNotFoundException;
import team.four.pas.exceptions.user.UserTypeException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.User;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/allocations"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AllocationControllerImpl {
    private final AllocationService allocationService;
    private final UserToDTO userToDTO;
    private final ResourceToDTO resourceToDTO;

    @GetMapping(
            {""}
    )
    public ResponseEntity<List<VMAllocation>> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getAll());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(
            {"/{id}"}
    )
    public ResponseEntity<VMAllocation> getById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.findById(id));
        } catch (AllocationIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (AllocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<VMAllocation> createAllocation(@RequestBody AllocationAddDTO addDTO) {
        UserDTO userDTO = addDTO.client();
        ResourceDTO resourceDTO = addDTO.vm();
        try {
            User client = userToDTO.clientFromClientDTO(userDTO);
            VirtualMachine resource = resourceToDTO.vmFromDTO(resourceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(allocationService.add(client, resource, Instant.now()));
        } catch (ResourceIdException | UserTypeException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (InactiveClientException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (ResourceAlreadyAllocatedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(
            {"/past/vm/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getPastVmAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getPastVm(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ResourceIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(
            {"/active/vm/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getActiveVmAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getActiveVm(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ResourceIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(
            {"/active/client/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getActiveClientAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getActiveClient(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UserIdException | UserTypeException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(
            {"/past/client/{id}"}
    )
    public ResponseEntity<List<VMAllocation>> getPastClientAllocations(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(allocationService.getPastClient(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UserIdException | UserTypeException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(
            {"/{id}/finish"}
    )
    public ResponseEntity<?> finishAllocation(@PathVariable String id) {
        try {
            allocationService.finishAllocation(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AllocationIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (AllocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(
            {"/{id}"}
    )
    public ResponseEntity<?> deleteAllocation(@PathVariable String id) {
        try {
            allocationService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (AllocationIdException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (AllocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AllocationNotActiveException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}