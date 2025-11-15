package team.four.pas.data;

import team.four.pas.data.users.Client;

import java.time.LocalDateTime;
import java.util.UUID;

public class VMAllocation implements Allocation {
    private UUID id;
    private Client client;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
