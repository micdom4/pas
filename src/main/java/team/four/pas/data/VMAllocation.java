package team.four.pas.data;

import team.four.pas.data.users.Client;

import java.time.LocalDateTime;

public class VMAllocation extends Allocation {
    private Client client;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
