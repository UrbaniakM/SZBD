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
    private Integer positionId;
    private Integer teamId = null;
    private Integer id;

    public Worker(Worker worker) {
        this.id = worker.getId();
        this.pesel = worker.getPesel();
        this.name = worker.getName();
        this.lastName = worker.getLastName();
        this.hireDate = worker.getHireDate();
        this.bonus = worker.getBonus();
        this.positionId = worker.getPositionId();
        this.teamId = worker.getTeamId();
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

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
