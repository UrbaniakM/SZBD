package GUI.Dialogs.Teams;

import Database.TeamsModification;
import Entities.Team;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class EditTeamDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Team teamAfterEdition;
    private Team teamBeforeEdition;

    // TODO WSZYSTKO

    public EditTeamDialog(Team team){

    }

    public Team popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            teamAfterEdition.setName(result.get().getName());
            teamAfterEdition.setCreationDate(result.get().getCreationDate());
            teamAfterEdition.setLeaderPesel(result.get().getPeselLeader());
            TeamsModification.editObject(teamBeforeEdition,teamAfterEdition);
            return teamAfterEdition;
        }
        return null;
    }

    private class Result {
        private String name;
        private Date creationDate;
        private String peselLeader;

        public Result(String name, LocalDate creationDate, String peselLeader) {
            this.name = name;
            this.creationDate = Date.valueOf(creationDate);
            this.peselLeader = peselLeader;
        }

        public String getName() {
            return name;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public String getPeselLeader() {
            return peselLeader;
        }
    }
}
