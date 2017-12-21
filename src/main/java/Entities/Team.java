package Entities;

import java.util.Date;

/**
 *
 */
public class Team {
    private int id;
    private String name;
    private Date creationDate;


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
