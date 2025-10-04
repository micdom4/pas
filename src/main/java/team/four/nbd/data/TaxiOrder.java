package team.four.nbd.data;

public class TaxiOrder extends Order {
    public enum Type {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    private int workerId;
    private Type type;
    private String startLocation;
    private String endLocation;
    private String licensePlate;
}
