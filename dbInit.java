package dataBaseLayer;

import java.sql.*;

/**
 * Created by Filip on 17-05-2016.
 */
public class dbInit {
    // this class will make all the databases when the program starts for
    // the first time
    static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Airline?useSSl=true";
    static Connection con;

    public void initialize()throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);

            con = DriverManager.getConnection(DATABASE_URL, "root", "password");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("CREATE DATABASE IF NOT EXISTS Airline;\n" +
                    "\n" +
                    "USE Airline;\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Plane(\n" +
                    "\tidPlane INT UNIQUE,\n" +
                    "    Name varchar (25),\n" +
                    "    FirstClass int,\n" +
                    "    EconomyClass int,\n" +
                    "    CoachClass int\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Employee(\n" +
                    "\tidEmployee int AUTO_INCREMENT UNIQUE,\n" +
                    "    Fname varchar(20),\n" +
                    "    Lname varchar(25),\n" +
                    "    Password varchar(40),\n" +
                    "    Position enum('CEO','Cashier','Secretary'),\n" +
                    "    Salary int\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS EmployeeAddress(\n" +
                    "\tidEmployee int,\n" +
                    "    Hometown varchar(40),\n" +
                    "    Country varchar(40),\n" +
                    "    Address varchar(40),\n" +
                    "    Zip_code int\n" +
                    ");\n" +
                    "\n" +
                    "INSERT INTO Employee\n" +
                    "\t(Fname, Lname, Password, Position, Salary)\n" +
                    "    values\n" +
                    "    ('Filip', 'Malek', 'MalekJeCar', 'Cashier', 100000),\n" +
                    "    ('Denis', 'Kutnar', '1111', 'CEO', 100000),\n" +
                    "    ('Patrik', 'Ando', 'PatrikTheStar', 'Secretary', 100000);\n" +
                    "    \n" +
                    "INSERT INTO Plane\n" +
                    "\t(idPlane,Name, FirstClass, EconomyClass, CoachClass )\n" +
                    "    values\n" +
                    "    (21281, 'Boeing 777', 16,194, 37),\n" +
                    "    (32939, 'Boeing 747', 8,262, 92 ),\n" +
                    "    (32222, 'Dash 8-Q400', 4, 70,2),\n" +
                    "    (23232, 'Airbus 319', 6,130, 5),\n" +
                    "    (90757, 'Airbus 320', 7, 150, 7);\n" +
                    "    \n" +
                    "    ");
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
