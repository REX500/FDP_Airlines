package dataBaseLayer;

import applicationLayer.Employee;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Filip on 17-05-2016.
 */
public class employeeDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?autoReconnect=true&useSSL=false";
    static Connection con;

    ArrayList<Employee> empArray;

    public ArrayList<Employee> getEmployee()throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM employee");

            empArray = new ArrayList<>();

            while(rs.next()){
                Employee employee = new Employee(rs.getInt("idEmployee"), rs.getString("Fname"), rs.getString("Lname"), rs.getString("Password"), rs.getString("Position"), rs.getInt("Salary"));
                empArray.add(employee);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return empArray;
    }
}
