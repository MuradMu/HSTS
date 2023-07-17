package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.*;
import il.cshaifasweng.HSTS.entities.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class ShowExecutedExamController implements Initializable {
    private Teacher teacher;
    private Exam exam;
    private ExamSubmittion SubmittedExam;

    @FXML
    private TextField TeacherDescription;

    @FXML
    private TableView<Question> QuestionsTable;
    private ShowExecutedExamsController PreviousController;

    public void updateLIST() {
        QuestionsTable.refresh();
    }
    public void setExecutedExam(ExamSubmittion examSubmittion) {
        this.SubmittedExam = examSubmittion;
        List<Question> questions = examSubmittion.getExam().getQuestions();
        Map<Question, String> chosenAnswers = examSubmittion.getAnswers();
        Map<Question, Integer> questionPoints = examSubmittion.getExam().getQuestionPoints();
        // Clear previous data
        QuestionsTable.getItems().clear();
        // Add submitted exam data to the table
        if(SubmittedExam.getChecked()){
            Map<Question,Integer> pointsmap = SubmittedExam.getQuestionPoints();
            Set<Question> questionsset = pointsmap.keySet();
            for(Question question : questions){
                String chosenAnswer = chosenAnswers.get(question);
                int points = questionPoints.getOrDefault(question, 0);
                question.setChosenAnswer(chosenAnswer);
                question.setPoints(points);
                for(Question question1 : questionsset){
                    if(question.getIdNum() == question1.getIdNum()){
                        question.setReceived_points(pointsmap.get(question1));
                    }
                }
                QuestionsTable.getItems().add(question);
            }
        }else{
            for (Question question : questions) {
                String chosenAnswer = chosenAnswers.get(question);
                int points = questionPoints.getOrDefault(question, 0);

                question.setChosenAnswer(chosenAnswer);
                question.setPoints(points);
                if(question.getCorrectAnswer().equals(question.getChosenAnswer())){
                    question.setReceived_points(points);
                }else{
                    question.setReceived_points(0);
                }
                QuestionsTable.getItems().add(question);
            }
        }
        QuestionsTable.refresh();
    }

//    public void setExam(Exam exam) {
//        this.exam = exam;
//        List<Question> ExamQuestions = exam.getQuestions();
//        QuestionsTable.setItems(FXCollections.observableArrayList(ExamQuestions));
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> Number_of_Question_Colum = new TableColumn<>("Number of Question");
        TableColumn<Question, String> Question_Text_Colum = new TableColumn<>("Question Text");
        TableColumn<Question, String> Answer_A_Colum = new TableColumn<>("Answer A");
        TableColumn<Question, String> Answer_B_Colum = new TableColumn<>("Answer B");
        TableColumn<Question, String> Answer_C_Colum = new TableColumn<>("Answer C");
        TableColumn<Question, String> Answer_D_Colum = new TableColumn<>("Answer D");
        TableColumn<Question, String> Chosen_Answer_Colum = new TableColumn<>("Chosen Answer");
        TableColumn<Question, String> Correct_Answer_Colum = new TableColumn<>("Correct Answer");
        TableColumn<Question, Integer> Question_Point_Colum = new TableColumn<>("Question Point");
        TableColumn<Question, Integer> Student_Point_Colum = new TableColumn<>("Student Point In Question");

        // Define property value factories for each column
        Number_of_Question_Colum.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        Question_Text_Colum.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        Answer_A_Colum.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        Answer_B_Colum.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        Answer_C_Colum.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        Answer_D_Colum.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        Chosen_Answer_Colum.setCellValueFactory(new PropertyValueFactory<>("chosenAnswer"));
        Correct_Answer_Colum.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        Question_Point_Colum.setCellValueFactory(new PropertyValueFactory<>("points"));

        Student_Point_Colum.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        // Set a cell value factory for the "Received Points" column to display and update the value
        Student_Point_Colum.setCellValueFactory(cellData -> {
            Question question = cellData.getValue();
            SimpleObjectProperty<Integer> property = new SimpleObjectProperty<>(question.getReceived_points());

            // Listen for changes to the cell value and update the question object accordingly
            property.addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    int points = newValue.intValue();
                    if (points >= 0 && points <= 100) {
                        question.setReceived_points(points);
                    } else {
                        // Invalid value entered, reset to previous value
                        property.set(oldValue);
                    }
                }
            });

            return property;
        });
        QuestionsTable.setEditable(true);

        QuestionsTable.getColumns().addAll(Number_of_Question_Colum, Question_Text_Colum, Answer_A_Colum, Answer_B_Colum, Answer_C_Colum, Answer_D_Colum, Chosen_Answer_Colum, Correct_Answer_Colum, Question_Point_Colum, Student_Point_Colum);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public void SubmitExam(ActionEvent actionEvent) throws IOException {
        for (Question question : QuestionsTable.getItems()) {
            int receivedPoints = question.getReceived_points();
            SubmittedExam.addPoints(question,receivedPoints);
        }
        MsgExamSubmittion msg = new MsgExamSubmittion("#UpdateSubmittedExam", SubmittedExam);
        SimpleClient.getClient().sendToServer(msg);
        SubmittedExam.setChecked(true);
        Node sourceNode = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
//        EventBus.getDefault().unregister(this);
        Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Message: \nData: %s",
                            "This Exam Checked Successfully"));
            alert.setTitle("Alert!");
            alert.setHeaderText("Message:");
            alert.show();
        });
        PreviousController.updateLIST();
    }
    public void setPreviousController(ShowExecutedExamsController controller){
        this.PreviousController = controller;
    }
}
