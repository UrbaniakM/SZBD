package Entities;


import java.sql.Date;

/**
 *
 */
public class Worker{
    private String pesel;
    private String name;
    private String lastName;
    private Date hireDate;
    private Integer bonus = null;
    private String positionName;
    private String teamName = null;

    public Worker(Worker worker) {
        this.pesel = worker.getPesel();
        this.name = worker.getName();
        this.lastName = worker.getLastName();
        this.hireDate = worker.getHireDate();
        this.bonus = worker.getBonus();
        this.positionName = worker.getPositionName();
        this.teamName = worker.getPositionName();
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

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
