package Entities;

import java.sql.Date;

/**
 *
 */
public class Holiday {
    private int id;
    private Date beginDate;
    private Date endDate;
    private Integer workerId;

    public Holiday(){

    }

    public Holiday(Holiday holiday){
        this.id = holiday.getId();
        this.beginDate = holiday.getBeginDate();
        this.endDate = holiday.getEndDate();
        this.workerId = holiday.getWorkerId();
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

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
