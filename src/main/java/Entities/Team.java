package Entities;


import java.sql.Date;

/**
 *
 */
public class Team {
    private String name;
    private Date creationDate;
    private Integer id;
    private Integer leaderId;
    private String leaderPesel;

    public Team(){

    }

    public Team(Team team){
        this.id = team.getId();
        this.name = team.getName();
        this.creationDate = team.getCreationDate();
        this.leaderId = team.getLeaderId();
        this.leaderPesel = team.getLeaderPesel();
    }

    public String getLeaderPesel() {
        return leaderPesel;
    }

    public void setLeaderPesel(String leaderPesel) {
        this.leaderPesel = leaderPesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
