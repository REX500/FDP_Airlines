package applicationLayer;

/**
 * Created by Patrik on 17-05-2016.
 */
public class Plane {
    private int idPlane;
    private String name;
    private int firstClass;
    private int economyClass;
    private int coachClass;

    public int getIdPlane() {
        return idPlane;
    }

    public void setIdPlane(int idPlane) {
        this.idPlane = idPlane;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(int firstClass) {
        this.firstClass = firstClass;
    }

    public int getEconomyClass() {
        return economyClass;
    }

    public void setEconomyClass(int economyClass) {
        this.economyClass = economyClass;
    }

    public int getCoachClass() {
        return coachClass;
    }

    public void setCoachClass(int coachClass) {
        this.coachClass = coachClass;
    }

    public Plane(int id, String name, int fclass, int eClass, int cClass){
        this.idPlane = id;
        this.name = name;
        this.firstClass = fclass;
        this.economyClass = eClass;
        this.coachClass = cClass;
    }

    public Plane(){

    }
}
