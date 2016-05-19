package applicationLayer;

/**
 * Created by Filip on 17-05-2016.
 */
public class Employee {
    private int idEmployee;
    private String fname;
    private String lname;
    private String password;
    private String position;
    private int salary;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Employee(int id, String fname, String lname, String password, String position, int salary){
        this.idEmployee = id;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.position = position;
        this.salary = salary;
    }

    public Employee(){

    }
}
