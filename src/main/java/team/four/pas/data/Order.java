package team.four.pas.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="orders")
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id", nullable = false)
    private Client client;

    @ManyToOne
    @NotNull
    private Worker worker;

    @Version
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
