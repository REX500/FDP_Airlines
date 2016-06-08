package dataBaseLayer;

import applicationLayer.Flight;
import applicationLayer.Plane;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Denis on 17-05-2016.
 */
public class planeDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?autoReconnect=true&useSSL=false";
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

    ArrayList<Plane> arrayList;
    public Plane getPlane(int planeId)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            String query = String.format("SELECT * FROM plane WHERE idPlane = '%d'", planeId);
            ResultSet rs = s.executeQuery(query);
            arrayList = new ArrayList<>();
            while (rs.next()){
                Plane plane = new Plane(rs.getInt("idPlane"),rs.getString("Name"),rs.getInt("FirstClass"), rs.getInt("EconomyClass"), rs.getInt("CoachClass"));
                arrayList.add(plane);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList.get(0);
    }

    public void deletePlane(int id) throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            String query = String.format("DELETE FROM plane WHERE idPlane = '%d'",id );
            s.executeUpdate(query);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addPlane(int id,String name, int fclass, int cclass, int eclass)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            String query = String.format("INSERT INTO plane (idPlane, Name, FirstClass, EconomyClass, CoachClass) VALUES  ('%d', '%s', '%d', '%d','%d')", id, name , fclass, eclass, cclass);
            s.executeUpdate(query);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
