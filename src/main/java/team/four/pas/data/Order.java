package team.four.pas.data;

import lombok.Data;

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

    public long getWorkerId(){
        return worker.getId();
    }

    public long getClientId(){
        return client.getId();
    }

    public void finishOrder() {
        active = false;
        endTime = LocalDateTime.now();
    }
}
