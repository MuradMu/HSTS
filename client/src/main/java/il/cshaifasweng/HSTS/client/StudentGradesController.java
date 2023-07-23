package il.cshaifasweng.HSTS.client;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.ExamSubmittion;
import il.cshaifasweng.HSTS.entities.Grade;
import il.cshaifasweng.HSTS.entities.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

        TableColumn<Grade, ExamSubmittion> ShowButtonCol = new TableColumn<>("Show");
        ShowButtonCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getExam()));
        ShowButtonCol.setCellFactory(param -> new TableCell<Grade, ExamSubmittion>() {
            private final Button showButton = new Button("Show");

            {
                showButton.setOnAction(event -> {
                    ExamSubmittion exam = getTableRow().getItem().getExam();
                    if (exam != null) {
                        // Code to handle the "Show" button action for the specific exam
                        ShowExam(exam);
                    }
                });
            }

            @Override
            protected void updateItem(ExamSubmittion exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setGraphic(null);
                } else {
                    setGraphic(showButton);
                }
            }
        });

        studentTable.getColumns().addAll(GradeNumColumn, courseNameColumn, gradeColumn, ShowButtonCol);
    }

    public void ShowExam(ExamSubmittion exam){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentShowExam.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            StudentShowExamController controller = loader.getController();
            controller.setExecutedExam(exam);
            controller.setPreviousController(this);
            Stage currentStage = new Stage();
            currentStage.setTitle("Executed Exams");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
