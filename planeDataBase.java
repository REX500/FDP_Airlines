package dataBaseLayer;

import applicationLayer.Plane;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Filip on 17-05-2016.
 */
public class planeDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?useSSl=true";
    static Connection con;

    ArrayList<Plane> planeArrayList;
    public ArrayList<Plane> getPlanes() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM plane");
            planeArrayList = new ArrayList<>();
            while(rs.next()){
                Plane plane = new Plane(rs.getInt("idPlane"),rs.getString("Name"),rs.getInt("FirstClass"), rs.getInt("EconomyClass"), rs.getInt("CoachClass"));
                planeArrayList.add(plane);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return planeArrayList;
    }
}
