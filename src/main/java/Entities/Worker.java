package Entities;


import java.sql.Date;

/**
 *
 */
public class Worker{
    private int id;
    private String pesel;
    private String name;
    private String lastName;
    private Date hireDate;
    private Date fireDate = null;
    private Integer hoursPerWeek = null;
    private Integer wage = null;
    private Integer idEtatu = 0;//null; TODO: foreign key, 0 for tests

    public Worker(Worker worker){
        this.id = worker.getId();
        this.pesel = worker.getPesel();
        this.name = worker.getName();
        this.lastName = worker.getLastName();
        this.hireDate = worker.getHireDate();
        this.fireDate = worker.getFireDate();
        this.wage = worker.getWage();
        this.idEtatu = worker.getIdEtatu();
    }

    public Worker(){

    }


    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toUpperCase();
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getFireDate() {
        return fireDate;
    }

    public void setFireDate(Date fireDate) {
        this.fireDate = fireDate;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public Integer getWage() {
        return wage;
    }

    public void setWage(Integer wage) {
        this.wage = wage;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public void setIdEtatu(int id) { this.idEtatu = id; }

    public int getIdEtatu() { return this.idEtatu; }
}
