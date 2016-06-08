package dataBaseLayer;

import applicationLayer.Customer;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Denis on 17-05-2016.
 */
public class customerDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?autoReconnect=true&useSSL=false";
    static Connection con;
    ArrayList<Customer> customers;
    public ArrayList<Customer> getCustomers()throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM customer");
            customers = new ArrayList<>();
            while (rs.next()){
                int customerId = rs.getInt("idCustomer");
                String name = rs.getString("FullName");
                String passId = rs.getString("Passport_ID");
                String flightId = rs.getString("Flight_ID");
                String planeClass = rs.getString("class");

                Customer customer = new Customer(customerId, name, passId, flightId, planeClass);

                customers.add(customer);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customers;
    }
    public void setCustomer(String name, String passport, String flightId, String planeClass)throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();

            String update = String.format("INSERT INTO customer (FullName, Passport_id, Flight_id, class) VALUES ('%s', '%s', '%s', '%s')", name, passport, flightId, planeClass);
            s.executeUpdate(update);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
