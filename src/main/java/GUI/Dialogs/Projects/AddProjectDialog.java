package GUI.Dialogs.Projects;

import Database.ProjectsModification;
import Entities.Project;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class AddProjectDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    // TODO WSZYSTKO

    public Project popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Project project = new Project();
            project.setName(result.get().getName());
            project.setBeginDate(result.get().getBeginDate());
            project.setEndDate(result.get().getEndDate());
            project.setTeamName(result.get().getTeamName());
            ProjectsModification.addObject(project);
            return project;
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
