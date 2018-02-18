package Entities;


import java.sql.Date;

/**
 *
 */
public class Project {
    private String name;
    private Date beginDate;
    private Date endDate = null;
    private Integer id;
    private Integer teamId;

    public Project(){

    }

    public Project(Project project){
        this.id = project.getId();
        this.name = project.getName();
        this.beginDate = project.getBeginDate();
        this.endDate = project.getEndDate();
        this.teamId = project.getTeamId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}
