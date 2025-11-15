package team.four.pas.data;

import lombok.Data;
import team.four.pas.data.Users.Client;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class Reservation {
    private UUID id;
    private Client client;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private float price;
    private String destination;
}
