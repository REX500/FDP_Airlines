package applicationLayer;

/**
 * Created by Filip on 17-05-2016.
 */
public class Customer {
    private int id;
    private String name;
    private String passId;
    private String flightId;
    private String planeClass;

    public String getPlaneClass() {
        return planeClass;
    }

    public void setPlaneClass(String planeClass) {
        this.planeClass = planeClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public Customer(int id, String name, String passId, String flightId, String planeClass){
        this.name = name;
        this.id = id;
        this.passId = passId;
        this.flightId = flightId;
        this.planeClass = planeClass;
    }

    public Customer(){

    }

}
