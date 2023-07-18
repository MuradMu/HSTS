package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentExamPageController implements Initializable {
    private Student student;
    private Exam exam;
    private Map<Question, String> answersMap = new HashMap<>();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label questionLabel;
    @FXML
    private Button choiceAButton;
    @FXML
    private Button choiceBButton;
    @FXML
    private Button choiceCButton;
    @FXML
    private Button choiceDButton;
    @FXML
    private Button submitButton;
    @FXML
    private Label remainingTimeLabel;
    @FXML
    private Label teacherNotesLabel;
    @FXML
    private TextArea teacherNotesField;

    private LocalTime startTime = null;
    private Duration duration;
    private boolean isExamSubmitted = false;
    private boolean isExamSubmitted1 = false;
    private StudentHomePageController PPcontroller;
    private int currentQuestionIndex = 0;

    public void setPPcontroller(StudentHomePageController PPcontroller) {
        this.PPcontroller = PPcontroller;
    }

    @Subscribe
    public void onExamSubmitEvent(ExamSubmitEvent examSubmitEvent) {
        if (!isExamSubmitted1) {
            PPcontroller.setTake_exam(false);
            isExamSubmitted1 = true;
            MsgExamSubmittion msg = examSubmitEvent.getMessage();
            if (msg.getRequest().equals("#ExamSubmittedSuccessfully")) {
                Platform.runLater(() -> {
                    Stage currentStage = (Stage) choiceAButton.getScene().getWindow();
                    currentStage.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Exam Submitted Successfully");
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
        teacherNotesField.setEditable(false);
    }

    public void submitExam(ActionEvent actionEvent) {
        if (!isExamSubmitted) {
            isExamSubmitted = true;
            ExamSubmittion examSubmission = new ExamSubmittion(student, exam, answersMap);
            MsgExamSubmittion msg = new MsgExamSubmittion("#ExamSubmitted", examSubmission);
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        showQuestion(examToShare.getQuestions().get(currentQuestionIndex)); // Show the first question initially
    }

    private Duration getRemainingTime() {
        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = startTime.plus(duration);
        Duration remainingTime = Duration.between(currentTime, endTime);

        // Check if remaining time is 0 and trigger button press
        if (remainingTime.isZero() || remainingTime.isNegative()) {
            Platform.runLater(() -> {
                    submitExam(null); // Trigger button press
            });
        }

        return remainingTime;
    }

    private void showQuestion(Question question) {
        questionLabel.setText(question.getQuestionText());
        choiceAButton.setText("A. " + question.getAnswerA());
        choiceBButton.setText("B. " + question.getAnswerB());
        choiceCButton.setText("C. " + question.getAnswerC());
        choiceDButton.setText("D. " + question.getAnswerD());
    }

    public void selectChoiceA(ActionEvent actionEvent) {
        answersMap.put(exam.getQuestions().get(currentQuestionIndex), "A");
    }

    public void selectChoiceB(ActionEvent actionEvent) {
        answersMap.put(exam.getQuestions().get(currentQuestionIndex), "B");
    }

    public void selectChoiceC(ActionEvent actionEvent) {
        answersMap.put(exam.getQuestions().get(currentQuestionIndex), "C");
    }

    public void selectChoiceD(ActionEvent actionEvent) {
        answersMap.put(exam.getQuestions().get(currentQuestionIndex), "D");
    }

    public void nextQuestion(ActionEvent actionEvent) {
        currentQuestionIndex++;
        if (currentQuestionIndex < exam.getQuestions().size()) {
            showQuestion(exam.getQuestions().get(currentQuestionIndex));
        } else {
            // All questions have been answered
                submitExam(null); // Trigger button press
        }
    }


}
