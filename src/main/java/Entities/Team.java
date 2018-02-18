package Entities;


import java.sql.Date;

/**
 *
 */
public class Team {
    private String name;
    private Date creationDate;
    private String leaderPesel;

    public Team(){

    }

    public Team(Team team){
        this.name = team.getName();
        this.creationDate = team.getCreationDate();
        this.leaderPesel = team.getLeaderPesel();
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

    public String getLeaderPesel() {
        return leaderPesel;
    }

    public void setLeaderPesel(String leaderPesel) {
        this.leaderPesel = leaderPesel;
    }
}
