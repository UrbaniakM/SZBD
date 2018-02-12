package GUI.Dialogs.Holidays;

import Database.HolidaysModification;
import Entities.Holiday;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class AddHolidayDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    // TODO WSZYSTKO

    public Holiday popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Holiday holiday = new Holiday();
            holiday.setPesel(result.get().getPesel());
            holiday.setBeginDate(result.get().getBeginDate());
            holiday.setEndDate(result.get().getEndDate());
            HolidaysModification.addObject(holiday);
            return holiday;
        }
        return null;
    }

    private class Result {
        private String pesel;
        private Date beginDate;
        private Date endDate;

        public Result(String pesel, LocalDate beginDate, LocalDate endDate){
            this.pesel = pesel;
            this.beginDate = Date.valueOf(beginDate);
            this.endDate = Date.valueOf(endDate);
        }

        public String getPesel() {
            return pesel;
        }


        public Date getBeginDate() {
            return beginDate;
        }


        public Date getEndDate() {
            return endDate;
        }
    }
}
