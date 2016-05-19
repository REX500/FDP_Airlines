package dataBaseLayer;

import java.sql.*;

/**
 * Created by Filip on 17-05-2016.
 */
public class customerDataBase {
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?useSSl=true";
    static Connection con;
    public void getCustomers()throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("CREATE DATABASE IF NOT EXISTS Airline;\n");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
