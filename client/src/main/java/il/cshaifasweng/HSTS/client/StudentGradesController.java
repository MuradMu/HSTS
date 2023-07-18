package il.cshaifasweng.HSTS.client;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.HSTS.entities.Grade;
import il.cshaifasweng.HSTS.entities.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class StudentGradesController  implements Initializable {
    private Student student;

    @FXML
    private Label studentNameLabel;

    @FXML
    private TableView<Grade> studentTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code
    }

    private void initializeTable() {
        // Set up the table columns
        TableColumn<Grade, Integer> GradeNumColumn = new TableColumn<>("Grade Num");
//        GradeNumColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        GradeNumColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        TableColumn<Grade, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse_name()));

        TableColumn<Grade, Integer> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGrade()).asObject());

        studentTable.getColumns().addAll(GradeNumColumn, courseNameColumn, gradeColumn);
    }

    private void populateTable() {
        List<Grade> grades = student.getGrades();

        studentTable.getItems().addAll(grades);
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentNameLabel.setText(student.getStudentName());
        initializeTable();
        populateTable();
    }
    public void updateGradeInTable(Grade updatedGrade) {
        // Find the grade in the table and update its values
        for (Grade grade : studentTable.getItems()) {
            if (grade.getId() == updatedGrade.getId()) {
                grade = updatedGrade;
                studentTable.refresh();
                break;
            }
        }
    }
}
