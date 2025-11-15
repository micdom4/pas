package team.four.pas.data;


public class TaxiOrder extends Order {

    public enum Type {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    private Type type;
    private String startLocation;
    private String licensePlate;
}
