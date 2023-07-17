package il.cshaifasweng.HSTS.client;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.HSTS.entities.Course;
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
    private Label CurrentStudentName;

    @FXML
    private TableView<Grade> StudentGradesTable;

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
        TableColumn<Course, String> Course_Name_Column = new TableColumn<>("Course Name");
        Course_Name_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse_name()));

        TableColumn<Grade, Integer> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGrade()).asObject());

        StudentGradesTable.getColumns().addAll(GradeNumColumn, gradeColumn);
    }

    private void populateTable() {
        List<Grade> grades = student.getGrades();

        StudentGradesTable.getItems().addAll(grades);
    }

    public void setStudent(Student student) {
        this.student = student;
        //this.CurrentStudentName.setText(student.getStudentName());
        initializeTable();
        populateTable();
    }
    public void updateGradeInTable(Grade updatedGrade) {
        // Find the grade in the table and update its values
        for (Grade grade : StudentGradesTable.getItems()) {
            if (grade.getId() == updatedGrade.getId()) {
                grade = updatedGrade;
                StudentGradesTable.refresh();
                break;
            }
        }
    }
}
