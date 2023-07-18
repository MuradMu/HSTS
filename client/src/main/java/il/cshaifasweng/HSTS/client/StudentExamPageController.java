package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.*;
import il.cshaifasweng.HSTS.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentExamPageController implements Initializable {
    private Student student;
    private Exam exam;
    private Map<Question, String> answersMap = new HashMap<>();
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private Button submitButton;
    @FXML
    private Label remainingTimeLabel; // Add this label to your FXML file
    @FXML TextArea teacherNotesField;

    private LocalTime startTime = null;
    private Duration duration;
    private boolean isExamSubmitted1 = false;
    private boolean isExamSubmitted2 = false;
    private StudentHomePageController PPcontrolelr;

    public void setPPcontrolelr(StudentHomePageController PPcontrolelr) {
        this.PPcontrolelr = PPcontrolelr;
    }

    @Subscribe
    public void onExamSubmitEvent(ExamSubmitEvent examSubmitEvent) {
        if (!isExamSubmitted2) {
            PPcontrolelr.setTake_exam(false);
            isExamSubmitted2 = true;
            MsgExamSubmittion msg = examSubmitEvent.getMessage();
            if (msg.getRequest().equals("#ExamSubmittedSuccessfully")) {
                Platform.runLater(() -> {
                    Stage currentStage = (Stage) questionTable.getScene().getWindow();
                    currentStage.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            String.format("Message: \nData: %s\nTimestamp: %s\n",
                                    "Exam Submitted Successfully",
                                    LocalTime.now())
                    );
                    alert.setTitle("Alert!");
                    alert.setHeaderText("Message:");
                    alert.show();
                    EventBus.getDefault().unregister(this);
                });
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        TableColumn<Question, Integer> questionNumCol = new TableColumn<>("Question_num");
        TableColumn<Question, String> questionCol = new TableColumn<>("Question");
        TableColumn<Question, String> aCol = new TableColumn<>("A");//manual set-> the header label is set to "A",
        TableColumn<Question, String> bCol = new TableColumn<>("B");
        TableColumn<Question, String> cCol = new TableColumn<>("C");
        TableColumn<Question, String> dCol = new TableColumn<>("D");

        questionNumCol.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        aCol.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        bCol.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        cCol.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        dCol.setCellValueFactory(new PropertyValueFactory<>("answerD"));

        TableColumn<Question, String> answerChoiceCol = new TableColumn<>("Answer Choice");
        answerChoiceCol.setPrefWidth(100); // Set the preferred width for the column

        answerChoiceCol.setCellFactory(column -> {
            return new TableCell<Question, String>() {
                private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

                {
                    choiceBox.getItems().addAll("A", "B", "C", "D");
                    choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        // Update the corresponding question's answer choice property
                        if (getTableRow() != null) {
                            Question question = getTableRow().getItem();
                            answersMap.put(question, newValue);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        choiceBox.setValue(item);
                        setGraphic(choiceBox);
                    }
                }
            };
        });

        questionTable.getColumns().addAll(
                questionNumCol, questionCol, aCol, bCol, cCol, dCol, answerChoiceCol
        );
        teacherNotesField.setEditable(false);
    }

    public void SubmitExam(ActionEvent actionEvent) throws IOException {
        if (!isExamSubmitted1) {
            isExamSubmitted1 = true;
            ExamSubmittion examSubmittion = new ExamSubmittion(student, exam, answersMap);
            MsgExamSubmittion msg = new MsgExamSubmittion("#ExamSubmitted", examSubmittion);
            SimpleClient.getClient().sendToServer(msg);
        }
    }

    public void setParameters(Student student, Exam examToShare) {
        this.student = student;
        this.exam = examToShare;

        teacherNotesField.setText(examToShare.getDescription_Student());
        // Calculate exam start time and duration based on examToShare
        startTime = LocalTime.now();
        int examDurationMinutes = exam.getTime();
        duration = Duration.ofMinutes(examDurationMinutes);
        // Start a background task to update the remaining time label
        Thread remainingTimeThread = new Thread(() -> {
            while (true) {
                try {
                    Duration remainingTime = getRemainingTime();
                    Platform.runLater(() -> {
                        // Update the remaining time label here
                        long minutes = remainingTime.toMinutes();
                        long seconds = remainingTime.minusMinutes(minutes).getSeconds();
                        remainingTimeLabel.setText(String.format("Remaining Time: %02d:%02d", minutes, seconds));
                    });
                    Thread.sleep(1000); // Wait for 1 second before updating again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        remainingTimeThread.setDaemon(true); // Set the thread as daemon to stop it when the application exits
        remainingTimeThread.start();
        ObservableList<Question> questions = FXCollections.observableArrayList();
        List<Question> questionList = exam.getQuestions();

        if (questionList.isEmpty()) {
//            System.out.print("\nSystem check Q.list is empty : ");
        } else {
            for (Question question : questionList) {
//                System.out.print("\nSystem check for Q.list: " + question.getQuestionText() + "\n");
                questions.add(question);
            }
        }
        questionTable.setItems(questions); // this should show the questions
    }
    private Duration getRemainingTime() {
        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = startTime.plus(duration);
        Duration remainingTime = Duration.between(currentTime, endTime);

        // Check if remaining time is 0 and trigger button press
        if (remainingTime.isZero() || remainingTime.isNegative()) {
            Platform.runLater(() -> {
                try {
                    SubmitExam(null); // Trigger button press
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return remainingTime;
    }
}
