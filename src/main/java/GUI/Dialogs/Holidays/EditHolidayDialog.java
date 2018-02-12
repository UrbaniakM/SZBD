package GUI.Dialogs.Holidays;

import Database.HolidaysModification;
import Entities.Holiday;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class EditHolidayDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Holiday holidayAfterEdition;
    private Holiday holidayBeforeEdition;

    // TODO WSZYSTKO

    public EditHolidayDialog(Holiday holiday){

    }

    public Holiday popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            holidayAfterEdition.setPesel(result.get().getPesel());
            holidayAfterEdition.setBeginDate(result.get().getBeginDate());
            holidayAfterEdition.setEndDate(result.get().getEndDate());
            HolidaysModification.editObject(holidayBeforeEdition,holidayAfterEdition);
            return holidayAfterEdition;
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
