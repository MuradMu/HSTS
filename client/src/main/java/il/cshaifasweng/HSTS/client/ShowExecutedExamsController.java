package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.ExamSubmittion;
import il.cshaifasweng.HSTS.entities.Teacher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowExecutedExamsController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<ExamSubmittion> ExamsTable;
    private Teacher teacher;
    private List<ExamSubmittion> executedExams;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public void setExecutedExams(List<ExamSubmittion> examSubmittionList) {
        this.executedExams = examSubmittionList;
        ExamsTable.setItems(FXCollections.observableArrayList(executedExams));
    }
    public void updateLIST() {
        ExamsTable.refresh();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        EventBus.getDefault().register(this);
        TableColumn<ExamSubmittion, Integer> IDCol = new TableColumn<>("Executed Exam ID");
        TableColumn<ExamSubmittion, Integer> ExamIDCol = new TableColumn<>("Exam ID");
        TableColumn<ExamSubmittion, String> StudentIDCol = new TableColumn<>("Student ID");
        TableColumn<ExamSubmittion, Boolean> CheckExamIDCol = new TableColumn<>("Check Exam");

        IDCol.setCellValueFactory(new PropertyValueFactory<>("id_num"));
        ExamIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getExam().getId_num()).asObject());
        StudentIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getId()));

        CheckExamIDCol.setCellValueFactory(new PropertyValueFactory<>("checked"));
        CheckExamIDCol.setCellFactory(param -> new TableCell<ExamSubmittion, Boolean>() {
            @Override
            protected void updateItem(Boolean isChecked, boolean empty) {
                super.updateItem(isChecked, empty);
                if (empty || isChecked == null) {
                    setGraphic(null);
                } else {
                    Button checkButton = new Button(isChecked ? "Edit" : "Check");
                    checkButton.setOnAction(event -> {
                        if (isChecked) {
                            // Handle "Edit" button action
                            handleCheckButtonAction(getTableRow().getItem());
                        } else {
                            // Handle "Check" button action
                            handleCheckButtonAction(getTableRow().getItem());
                        }
                    });
                    setGraphic(checkButton);
                }
            }
        });
        ExamsTable.getColumns().addAll(IDCol, ExamIDCol, StudentIDCol, CheckExamIDCol);
    }

    private void handleEditButtonAction(ExamSubmittion examSubmittion) {
        handleCheckButtonAction(examSubmittion);
    }

    private void handleCheckButtonAction(ExamSubmittion examSubmittion) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowExecutedExam.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            ShowExecutedExamController controller = loader.getController();
            controller.setExecutedExam(examSubmittion);
            controller.setPreviousController(this);
            Stage currentStage = new Stage();
            currentStage.setTitle("Executed Exams");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
