package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.Teacher;
import il.cshaifasweng.HSTS.entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowExamsController implements Initializable {
    private Teacher teacher;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Exam> ExamsTable;
    public void updateLIST() {
        List<Exam> teacherExams = teacher.getExams();
        ExamsTable.setItems(FXCollections.observableArrayList(teacherExams));
        ExamsTable.refresh();
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        List<Exam> teacherExams = teacher.getExams();
        ExamsTable.setItems(FXCollections.observableArrayList(teacherExams));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        TableColumn<Exam, Integer> ExamIDCol = new TableColumn<>("ID");
        TableColumn<Exam, String> CourseIDCol = new TableColumn<>("Course ID");
        TableColumn<Exam, String> TeacherIDCol = new TableColumn<>("Teacher ID");
        TableColumn<Exam, Integer> TimeCol = new TableColumn<>("Time");
        TableColumn<Exam, Exam> ShowButtonCol = new TableColumn<>("Show");
        TableColumn<Exam, Exam> EditButtonCol = new TableColumn<>("Edit");

        ExamIDCol.setCellValueFactory(new PropertyValueFactory<>("id_num"));
        CourseIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse().getId()));
        TeacherIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeacher().getId()));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        ShowButtonCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        ShowButtonCol.setCellFactory(param -> new TableCell<Exam, Exam>() {
            private final Button showButton = new Button("Show");

            {
                showButton.setOnAction(event -> {
                    Exam exam = getTableRow().getItem();
                    if (exam != null) {
                        // Code to handle the "Show" button action for the specific exam
                        ShowExam(exam);
                    }
                });
            }

            @Override
            protected void updateItem(Exam exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setGraphic(null);
                } else {
                    setGraphic(showButton);
                }
            }
        });

        EditButtonCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        EditButtonCol.setCellFactory(param -> new TableCell<Exam, Exam>() {
            private final Button showButton = new Button("Edit");

            {
                showButton.setOnAction(event -> {
                    Exam exam = getTableRow().getItem();
                    if (exam != null) {
                        // Code to handle the "Show" button action for the specific exam
                        EditExam(exam);
                    }
                });
            }

            @Override
            protected void updateItem(Exam exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setGraphic(null);
                } else {
                    setGraphic(showButton);
                }
            }
        });

        ExamsTable.getColumns().addAll(ExamIDCol, CourseIDCol, TeacherIDCol, TimeCol, ShowButtonCol, EditButtonCol);
    }

    public void ShowExam(Exam exam){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowExam.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            ShowExamController controller = loader.getController();
            controller.setTeacher(teacher);
            controller.setExam(exam);
            Stage currentStage = new Stage();
            currentStage.setTitle("Exam number " + exam.getId_num());
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void EditExam(Exam exam){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditExam.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            EditExamController controller = loader.getController();
            controller.setPreviousController(this);
            controller.setTeacher(teacher);
            controller.setExam(exam);
            controller.initializee();
            Stage currentStage = new Stage();
            currentStage.setTitle("Exam number " + exam.getId_num());
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Subscribe
    public void onShareExamEvent(ShareExamEvent shareExamEvent){
        setTeacher(shareExamEvent.getShareExamMsg().getExamToShare().getTeacher());
    }


}
