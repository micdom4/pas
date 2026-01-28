package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ResourceAddDTO(
        @Min(value = 1, message = "cpu number must be between 1 and 100")
        @Max(value = 100, message = "cpu number must be between 1 and 100")
        int cpuNumber,

        @Min(value = 1, message = "ramGiB must be between 1 and 1024 GB")
        @Max(value = 1024, message = "ramGiB must be between 1 and 1024 GB")
        int ramGiB,

        @Min(value = 1, message = "storageGiB must be between 1 GiB and 1 PiB")
        @Max(value = 1048576, message = "storageGiB must be between 1 GiB and 1 PiB")
        int storageGiB
) {
}
