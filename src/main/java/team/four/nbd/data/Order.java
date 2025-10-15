package team.four.nbd.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private float price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private String destination;

    @Override
    public String toString() {
        return "id: " + orderId + " price: " + price + " " + client.toString();
    }

    public long getWorkerId(){
        return worker.getId();
    }

    public long getClientId(){
        return client.getId();
    }

}
