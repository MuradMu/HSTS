package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Question;
import il.cshaifasweng.HSTS.entities.QuestionMsg;
import il.cshaifasweng.HSTS.entities.Teacher;
import il.cshaifasweng.HSTS.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.stage.Stage;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
public class AddQuestionController implements Initializable{
    private Stage stage;
    private Teacher teacher;
    @FXML
    private Button AddQuestionButton;

    @FXML
    private TextField Answer_A;

    @FXML
    private TextField Answer_B;

    @FXML
    private TextField Answer_C;

    @FXML
    private TextField Answer_D;

    @FXML
    private ChoiceBox<String> CorrectAnswerBox;

    @FXML
    private TableView<Question> QuestionTable;

    @FXML
    private TextField QuestionText;

    private TableColumn<Question, Void> editCol;
    private List<Question> questions = new ArrayList<>();

    public void updateLIST() {
        QuestionTable.refresh();
    }
    public void initializee() {
        ObservableList<Question> QuestionsForTeacher = FXCollections.observableArrayList();
        TableColumn<Question, Integer> questionNumCol = new TableColumn<>("Question_num");
        TableColumn<Question, String> questionCol = new TableColumn<>("Question");
        TableColumn<Question, String> aCol = new TableColumn<>("A");//manual set-> the header label is set to "A",
        TableColumn<Question, String> bCol = new TableColumn<>("B");
        TableColumn<Question, String> cCol = new TableColumn<>("C");
        TableColumn<Question, String> dCol = new TableColumn<>("D");
        TableColumn<Question, String> answerCol = new TableColumn<>("Answer");

        // Define property value factories for each column
        questionNumCol.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        aCol.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        bCol.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        cCol.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        dCol.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        QuestionTable.getColumns().addAll(
                questionNumCol, questionCol, aCol, bCol, cCol, dCol, answerCol
        );

        List<Question> questionList=teacher.getTeacherQuestionsList();// todo check
        if(questionList.isEmpty()){
//            System.out.print("\nSystem check Q.list is empty : ");
        }else {
            for(Question question : questionList){
//                {System.out.print("\nSystem check for Q.list: " + question.getQuestionText() + "\n");}
                QuestionsForTeacher.add(question);
            }
        }

        QuestionTable.setItems(QuestionsForTeacher);// this should show the questions

        // Create the edit column
        editCol = new TableColumn<>("Edit");

        // Set the cell factory to create custom cells with edit buttons
        editCol.setCellFactory(createEditButtonCellFactory());

        // Add the edit column to the table
        QuestionTable.getColumns().add(editCol);
    }
    private Callback<TableColumn<Question, Void>, TableCell<Question, Void>> createEditButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Question, Void> call(TableColumn<Question, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");

                    {
                        // Handle button action
                        editButton.setOnAction((ActionEvent event) -> {
                            Question question = getTableRow().getItem();
                            // Handle the edit action for the clicked question
                            handleEditQuestion(question);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        };
    }
    private void handleEditQuestion(Question question) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditQuestion.fxml"));
            try {
                AnchorPane newScene = loader.load();
                Stage currentStage = new Stage();
                Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
                currentStage.setTitle("Edit Question");
                currentStage.setScene(scene);
                EditQuestionController controller = loader.getController();
                controller.setQuestion(question);
                controller.setPreviousLoader(this);
                controller.initializee();
                currentStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void AddQuestion(ActionEvent actionEvent) throws IOException {
        ObservableList<CheckBox> selectedCheckboxes = FXCollections.observableArrayList();
        String QuestionTxt;
        List<String> answers = new ArrayList<>();
        String correctAnswer;
        answers.add(Answer_A.getText());
        answers.add(Answer_B.getText());
        answers.add(Answer_C.getText());
        answers.add(Answer_D.getText());
        QuestionTxt = QuestionText.getText();
        correctAnswer = CorrectAnswerBox.getValue();
        Question question = new Question(QuestionTxt, answers, correctAnswer,teacher);
        QuestionMsg msg1 = new QuestionMsg("#AddQuestion", question, teacher);
        SimpleClient.getClient().sendToServer(msg1);
    }
    @Subscribe
    public void onReceivingQuestionEvent(ReceivingQuestionEvent message){
        Answer_A.setText("");
        Answer_B.setText("");
        Answer_C.setText("");
        Answer_D.setText("");
        QuestionText.setText("");
        Platform.runLater(() -> CorrectAnswerBox.setValue(null));
        Question q = message.getMessage().getQuestion();
        teacher.addQuestion(q);
        QuestionTable.getItems().add(q);
        QuestionTable.refresh();
        // setTeacher(message.getMessage().getTeacherWhoCreate());
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
    public void onCloseWindow() {
        EventBus.getDefault().unregister(this);
    }

    public void setupWindowCloseHandler() {
        stage.setOnCloseRequest(event -> {
            onCloseWindow();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
