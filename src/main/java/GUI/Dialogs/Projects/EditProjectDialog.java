package GUI.Dialogs.Projects;

import Database.ProjectsModification;
import Entities.Project;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class EditProjectDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Project projectAfterEdition;
    private Project projectBeforeEdition;

    // TODO WSZYSTKO

    public EditProjectDialog(Project project){

    }

    public Project popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            projectAfterEdition.setName(result.get().getName());
            projectAfterEdition.setBeginDate(result.get().getBeginDate());
            projectAfterEdition.setEndDate(result.get().getEndDate());
            projectAfterEdition.setTeamName(result.get().getTeamName());
            ProjectsModification.editObject(projectBeforeEdition,projectAfterEdition);
            return projectAfterEdition;
        }
        return null;
    }

    private class Result {
        private String name;
        private Date beginDate;
        private Date endDate;
        private String teamName;

        public Result(String name, LocalDate beginDate, LocalDate endDate, String teamName) {
            this.name = name;
            this.beginDate = Date.valueOf(beginDate);
            this.endDate = Date.valueOf(endDate);
            this.teamName = teamName;
        }

        public String getName() {
            return name;
        }

        public Date getBeginDate() {
            return beginDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public String getTeamName() {
            return teamName;
        }
    }
}