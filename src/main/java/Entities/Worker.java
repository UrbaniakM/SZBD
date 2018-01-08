package Entities;


import java.sql.Date;

/**
 *
 */
public class Worker{
    private int id;
    private Integer pesel;
    private String name;
    private String lastName;
    private Date hireDate;
    private Date fireDate = null;
    private Integer hoursPerWeek = null;
    private Float wage = null;

    public int getPesel() {
        return pesel;
    }

    public void setPesel(int pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Float getWage() {
        return wage;
    }

    public void setWage(Float wage) {
        this.wage = wage;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }
}
