package dataBaseLayer;

import applicationLayer.Employee;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Patrik on 24-05-2016.
 */
public class employeeAddressDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?autoReconnect=true&useSSL=false";
    static Connection con;
    static ArrayList<Employee> employeeArrayList;
    public static Employee getAddressInfo(int empId, String fname, String lname, String pass, String pos, int salary) throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM employeeaddress");
            employeeArrayList = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("idEmployee");
                String hometown = rs.getString("Hometown");
                String country = rs.getString("Country");
                String address = rs.getString("Address");
                int zip = rs.getInt("Zip_code");

                if(id == empId){
                    Employee employee = new Employee(empId, fname, lname, pass, pos, salary);
                    employee.setAddress(address);
                    employee.setCountry(country);
                    employee.setZip(zip);
                    employee.setHometown(hometown);
                    employeeArrayList.add(employee);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return employeeArrayList.get(0);
    }
}
