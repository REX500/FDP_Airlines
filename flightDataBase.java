package dataBaseLayer;

import applicationLayer.Flight;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Filip on 18-05-2016.
 */
public class flightDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline";
    static Connection con;

    ArrayList<Flight> flightArrayList;
    public ArrayList<Flight> getFlights() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM flights");
            flightArrayList = new ArrayList<>();
            while(rs.next()){
                int flightId = rs.getInt("idFlights");
                int planeId = rs.getInt("Plane_id");
                String dest = rs.getString("Destination");
                String fromDate = rs.getString("From_date");
                String toDate = rs.getString("Return_date");
                int passE = rs.getInt("passExpected");
                int fC = rs.getInt("firstClass");
                int eC = rs.getInt("economyClass");
                int cC = rs.getInt("coachClass");

                Flight flight = new Flight(flightId, planeId, dest, passE, toDate, fromDate, fC, eC, cC);

                flightArrayList.add(flight);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flightArrayList;
    }

    public void scheduleFlight(int planeId, String dest, String from, String to, int passExpected)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            int passOnBoard = 0;

            String update = String.format("INSERT INTO flights (Plane_id, Destination, From_date, Return_date, passExpected, passOnBoard) VALUES ('%d', '%s', '%s', '%s','%d','%d')", planeId, dest, from, to, passExpected, passOnBoard);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Flight> arrayList;
    public Flight selectAFlight(int flightId)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            String query = String.format("SELECT * FROM flights WHERE idFlights = '%d'", flightId);
            ResultSet rs = s.executeQuery(query);
            arrayList = new ArrayList<>();
            while (rs.next()){
                int fId = rs.getInt("idFlights");
                int planeId = rs.getInt("Plane_id");
                String dest = rs.getString("Destination");
                String fromDate = rs.getString("From_date");
                String toDate = rs.getString("Return_date");
                int passE = rs.getInt("passExpected");
                int fC = rs.getInt("firstClass");
                int eC = rs.getInt("economyClass");
                int cC = rs.getInt("coachClass");

                Flight flight = new Flight(fId, planeId, dest, passE, toDate, fromDate, fC, eC, cC);
                arrayList.add(flight);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList.get(0);
    }

    public void setFlightFirstSeats(int flightId)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            int passOnBoard = 0;

            String update = String.format("UPDATE flights SET firstClass = firstClass +1 WHERE idFlights = '%d'", flightId);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFlightEconomySeats(int flightId)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            int passOnBoard = 0;

            String update = String.format("UPDATE flights SET economyClass = economyClass +1 WHERE idFlights = '%d'", flightId);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFlightCoachSeats(int flightId)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            int passOnBoard = 0;

            String update = String.format("UPDATE flights SET coachClass = coachClass +1 WHERE idFlights = '%d'", flightId);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void cancelFlight(int flightID, String planeClass)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();

            String update = String.format("UPDATE flights SET '%s' = '%s' -1 WHERE idFlights = '%d'", planeClass, planeClass, flightID);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelCoachFlight(int flightID)throws  SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();

            String update = String.format("UPDATE flights SET coachClass = coachClass -1 WHERE idFlights = '%d'",flightID);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelFirstFlight(int flightID)throws  SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();

            String update = String.format("UPDATE flights SET firstClass = firstClass -1 WHERE idFlights = '%d'",flightID);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelEconomyFlight(int flightID)throws  SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();

            String update = String.format("UPDATE flights SET economyClass = economyClass -1 WHERE idFlights = '%d'",flightID);
            s.executeUpdate(update);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
