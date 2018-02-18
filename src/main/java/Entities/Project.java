package Entities;


import java.sql.Date;

/**
 *
 */
public class Project {
    private String name;
    private Date beginDate;
    private Date endDate = null;
    private String teamName;

    public Project(){

    }

    public Project(Project project){
        this.name = project.getName();
        this.beginDate = project.getBeginDate();
        this.endDate = project.getEndDate();
        this.teamName = project.getTeamName();
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
