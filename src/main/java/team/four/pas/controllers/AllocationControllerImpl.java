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

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
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


}