package team.four.nbd.data;

import java.util.Date;

public abstract class Order {
    private int orderId;
    private int clientId;
    private float price;
    private Date startTime;
    private Date endTime;
    private boolean active;
    private String destination;
}
