package GUI.Dialogs.Teams;

import Database.TeamsModification;
import Entities.Team;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class AddTeamDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    // TODO WSZYSTKO

    public Team popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Team team = new Team();
            team.setName(result.get().getName());
            team.setCreationDate(result.get().getCreationDate());
            team.setLeaderPesel(result.get().getPeselLeader());
            TeamsModification.addObject(team);
            return team;
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
