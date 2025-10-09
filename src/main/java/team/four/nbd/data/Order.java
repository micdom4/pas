package team.four.nbd.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="orders")
public abstract class Order {

    @Id
    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id", nullable = false)
    private Client client;
    private float price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private String destination;
}
