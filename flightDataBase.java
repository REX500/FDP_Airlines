package dataBaseLayer;

import applicationLayer.Flight;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Filip on 18-05-2016.
 */
public class flightDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?useSSl=true";
    static Connection con;

    ArrayList<Flight> flightArrayList;
    public ArrayList<Flight> getFlights() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM flights");

            while(rs.next()){
                int flightId = rs.getInt("idFlights");
                int planeId = rs.getInt("Plane_id");
                String dest = rs.getString("Destination");
                String fromDate = rs.getString("From_date");
                String toDate = rs.getString("Return_date");
                int passE = rs.getInt("passExpected");
                int passO = rs.getInt("passOnBoard");

                Flight flight = new Flight(flightId, planeId, dest, passE, passO, toDate, fromDate);
                flightArrayList.add(flight);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flightArrayList;
    }
}
