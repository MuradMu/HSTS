package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.Question;
import il.cshaifasweng.HSTS.entities.Teacher;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ShowExamController implements Initializable {
    private Teacher teacher;
    private Exam exam;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField TeacherDescription;

    @FXML
    private TableView<Question> QuestionsTable;

    public void updateLIST() {
        QuestionsTable.refresh();
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        List<Question> ExamQuestions = exam.getQuestions();
        QuestionsTable.setItems(FXCollections.observableArrayList(ExamQuestions));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> questionNumCol = new TableColumn<>("Question_num");
        TableColumn<Question, String> questionCol = new TableColumn<>("Question");
        TableColumn<Question, String> aCol = new TableColumn<>("A");//manual set-> the header label is set to "A",
        TableColumn<Question, String> bCol = new TableColumn<>("B");
        TableColumn<Question, String> cCol = new TableColumn<>("C");
        TableColumn<Question, String> dCol = new TableColumn<>("D");
        TableColumn<Question, String> answerCol = new TableColumn<>("Answer");
        TableColumn<Question, Integer> pointsCol = new TableColumn<>("Points");


        // Define property value factories for each column
        questionNumCol.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        aCol.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        bCol.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        cCol.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        dCol.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        pointsCol.setCellValueFactory(cellData -> {
            Question question = cellData.getValue();
            Map<Question, Integer> questionPointsMap = exam.getQuestionPoints();
            Integer points = questionPointsMap.get(question);
            TeacherDescription.setText(exam.getDescription_Teacher());
            return new SimpleObjectProperty<>(points);
        });

        QuestionsTable.getColumns().addAll(
                questionNumCol, questionCol, aCol, bCol, cCol, dCol, answerCol, pointsCol
        );
    }


    public void AutoExam(ActionEvent actionEvent) {
        // now if we try to share exam that already shared , alert will show and scene will close
        // else we jump to shareExam controller to set the password for the exam
        if (exam.getShared()) {
            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        String.format("Message: \nData: %s",
                                "This Exam Already Shared In DataBase"));
                alert.setTitle("Alert!");
                alert.setHeaderText("Message:");
                alert.show();
            });
        } else {
            // we enter here so the exam not shared
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShareExam.fxml"));
                AnchorPane newScene = loader.load();
                Scene scene = new Scene(newScene);
                ShareExamController controller = loader.getController();
                //  ShareExamController controller = loader.getController();
                controller.setTeacher(teacher);
                controller.setExam(exam);
                controller.setPreviousLoader(this);
                Stage currentStage = new Stage();
                currentStage.setTitle("Sharing Exam id: " + exam.getId_num());
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
