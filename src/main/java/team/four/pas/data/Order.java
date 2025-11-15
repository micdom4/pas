package team.four.pas.data;

import lombok.Data;
import team.four.pas.data.Users.Client;
import team.four.pas.data.Users.Worker;

import java.time.LocalDateTime;

@Data
public abstract class Order {

    private long orderId;

    private Client client;

    private Worker worker;

    private long version;

    private float price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private String destination;

    public void finishOrder() {
        active = false;
        endTime = LocalDateTime.now();
    }
}
