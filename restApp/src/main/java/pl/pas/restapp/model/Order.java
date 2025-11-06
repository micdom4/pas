package pl.pas.restapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@Document("orders")
public class Order {

    public enum TaxiType {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    @Id
    private long id;
    private long clientId;
    private long workerId;

    private float price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private String destination;

    private TaxiType type;
    private String startLocation;
    private String licensePlate;

    @Version
    private long version;

    public Order() {}

    public Order(Client client, Worker worker, TaxiType taxiType) {
        this.clientId = client.getId();
        this.workerId = worker.getId();
        this.id = client.getId();
    }

    public void finishOrder() {
        active = false;
        endTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + id +
                ", clientId=" + clientId +
                ", workerId=" + workerId +
                ", price=" + price +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", active=" + active +
                ", destination='" + destination + '\'' +
                ", taxiType=" + type +
                ", startLocation='" + startLocation + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}
