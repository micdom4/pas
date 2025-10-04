package data;

public class TaxiOrder {
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
}
