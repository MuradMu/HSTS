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

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

//    public void setExam(Exam exam) {
//        this.exam = exam;
//        List<Question> ExamQuestions = exam.getQuestions();
//        QuestionsTable.setItems(FXCollections.observableArrayList(ExamQuestions));
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> questionNumCol = new TableColumn<>("Question Number");
        TableColumn<Question, String> questionCol = new TableColumn<>("Question");
        TableColumn<Question, String> aCol = new TableColumn<>("A");
        TableColumn<Question, String> bCol = new TableColumn<>("B");
        TableColumn<Question, String> cCol = new TableColumn<>("C");
        TableColumn<Question, String> dCol = new TableColumn<>("D");
        TableColumn<Question, String> chosenAnswerCol = new TableColumn<>("Chosen Answer");
        TableColumn<Question, String> correctAnswerCol = new TableColumn<>("Correct Answer");
        TableColumn<Question, Integer> pointsCol = new TableColumn<>("Points");
        TableColumn<Question, Integer> studentPointsCol = new TableColumn<>("Received Points");

        // Define property value factories for each column
        questionNumCol.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        aCol.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        bCol.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        cCol.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        dCol.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        chosenAnswerCol.setCellValueFactory(new PropertyValueFactory<>("chosenAnswer"));
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        studentPointsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        // Set a cell value factory for the "Received Points" column to display and update the value
        studentPointsCol.setCellValueFactory(cellData -> {
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

        QuestionsTable.getColumns().addAll(
                questionNumCol, questionCol, aCol, bCol, cCol, dCol,
                chosenAnswerCol, correctAnswerCol, pointsCol, studentPointsCol
        );
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
