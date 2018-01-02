package Entities;

import java.util.Date;

/**
 *
 */
public class TimeSpent {
    private Date beginDate;
    private Date endDate = null;

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
}
