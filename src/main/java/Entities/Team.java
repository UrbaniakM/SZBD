package Entities;

import java.util.Date;

/**
 *
 */
public class Team {
    private String name;
    private Date creationDate;
    private String leaderPesel;

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
