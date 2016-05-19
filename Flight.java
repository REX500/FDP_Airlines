package applicationLayer;

/**
 * Created by Filip on 18-05-2016.
 */
public class Flight {
    private int idFlights;
    private int plane_id;
    private String destination;
    private int passExpected;
    private int passOnBoard;
    private String returnDate;
    private String fromDate;

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

    public int getPassOnBoard() {
        return passOnBoard;
    }

    public void setPassOnBoard(int passOnBoard) {
        this.passOnBoard = passOnBoard;
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

    public Flight(int id, int planeId, String destination, int passExpected, int passOnBoard, String returnDate, String fromDate){
        this.idFlights = id;
        this.plane_id = planeId;
        this.destination = destination;
        this.passExpected = passExpected;
        this.passOnBoard = passOnBoard;
        this.returnDate = returnDate;
        this.fromDate = fromDate;
    }

}
