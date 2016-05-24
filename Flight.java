package applicationLayer;

/**
 * Created by Filip on 18-05-2016.
 */
public class Flight {
    private int idFlights;
    private int plane_id;
    private String destination;
    private int passExpected;
    private String returnDate;
    private String fromDate;
    private int firstClass;
    private int economyClass;
    private int coachClass;

    public int getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(int firstClass) {
        this.firstClass = firstClass;
    }

    public int getEconomyClass() {
        return economyClass;
    }

    public void setEconomyClass(int economyClass) {
        this.economyClass = economyClass;
    }

    public int getCoachClass() {
        return coachClass;
    }

    public void setCoachClass(int coachClass) {
        this.coachClass = coachClass;
    }

    public int getIdFlights() {
        return idFlights;
    }

    public void setIdFlights(int idFlights) {
        this.idFlights = idFlights;
    }

    public int getPlane_id() {
        return plane_id;
    }

    public void setPlane_id(int plane_id) {
        this.plane_id = plane_id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPassExpected() {
        return passExpected;
    }

    public void setPassExpected(int passExpected) {
        this.passExpected = passExpected;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Flight(int id, int planeId, String destination, int passExpected, String returnDate, String fromDate, int firstClass, int economyClass, int coachClass){
        this.idFlights = id;
        this.plane_id = planeId;
        this.destination = destination;
        this.passExpected = passExpected;
        this.returnDate = returnDate;
        this.fromDate = fromDate;
        this.firstClass = firstClass;
        this.economyClass = economyClass;
        this.coachClass = coachClass;
    }

}
