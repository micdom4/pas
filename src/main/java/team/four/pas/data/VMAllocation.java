package team.four.pas.data;
import lombok.NonNull;
import team.four.pas.data.users.Client;

import java.time.LocalDateTime;
import java.util.UUID;

public class VMAllocation extends Allocation {
    private Client client;
    private VirtualMachine vm;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public VMAllocation(@NonNull UUID uuid) {
        super(uuid);
    }
}
