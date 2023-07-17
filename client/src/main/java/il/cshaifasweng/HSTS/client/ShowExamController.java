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

//    public void updateLIST() {
//        QuestionsTable.refresh();
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> Number_of_Question_Colum = new TableColumn<>("Number of Question");
        TableColumn<Question, String> Question_Text_Colum = new TableColumn<>("Question Text");
        TableColumn<Question, String> Answer_A_Colum = new TableColumn<>("Answer A");//manual set-> the header label is set to "A",
        TableColumn<Question, String> Answer_B_Colum = new TableColumn<>("Answer B");
        TableColumn<Question, String> Answer_C_Colum = new TableColumn<>("Answer C");
        TableColumn<Question, String> Answer_D_Colum = new TableColumn<>("Answer D");
        TableColumn<Question, String> Correct_Answer_Colum = new TableColumn<>("Correct Answer");
        TableColumn<Question, Integer> Question_Point_Colum = new TableColumn<>("Question Point");


        // Define property value factories for each column
        Number_of_Question_Colum.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        Question_Text_Colum.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        Answer_A_Colum.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        Answer_B_Colum.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        Answer_C_Colum.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        Answer_D_Colum.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        Correct_Answer_Colum.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        Question_Point_Colum.setCellValueFactory(cellData -> {
            Question question = cellData.getValue();
            Map<Question, Integer> QuestionPointsMap = exam.getQuestionPoints();
            Integer points = QuestionPointsMap.get(question);
            TeacherDescription.setText(exam.getDescription_Teacher());
            return new SimpleObjectProperty<>(points);
        });

        QuestionsTable.getColumns().addAll(Number_of_Question_Colum, Question_Text_Colum, Answer_A_Colum, Answer_B_Colum, Answer_C_Colum, Answer_D_Colum, Correct_Answer_Colum, Question_Point_Colum);
    }
    public void setExam(Exam exam) {
        this.exam = exam;
        List<Question> ExamQuestions = exam.getQuestions();
        QuestionsTable.setItems(FXCollections.observableArrayList(ExamQuestions));
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
    public Teacher getTecher(){return this.teacher;}
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
