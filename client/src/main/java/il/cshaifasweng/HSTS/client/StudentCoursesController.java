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
public class StudentCoursesController implements Initializable{

    private Student student;
    @FXML
    private TableView<Course> StudentCoursesTable;

    private void initializeTable() {
        TableColumn<Course, Integer> Course_Index_Colum = new TableColumn<>("");
//        GradeNumColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        Course_Index_Colum.setCellFactory(column -> new TableCell<>() {
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
        TableColumn<Course, String> Course_Id_Column = new TableColumn<>("Course Id");
        Course_Name_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse_name()));
        Course_Id_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));

        StudentCoursesTable.getColumns().addAll(Course_Index_Colum, Course_Name_Column, Course_Id_Column);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code
    }
    private void populateTable() {
        List<Course> Courses = student.getCourses();
        StudentCoursesTable.getItems().addAll(Courses);
    }

    public void setStudent(Student student) {
        this.student = student;
        initializeTable();
        populateTable();
    }
}
