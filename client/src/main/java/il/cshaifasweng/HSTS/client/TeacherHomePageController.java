package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgBringExecutedExams;
import il.cshaifasweng.HSTS.entities.Teacher;
import il.cshaifasweng.HSTS.entities.User;
import il.cshaifasweng.HSTS.entities.*;


import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TeacherHomePageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addQuestionButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button ExecutedExamsButton;

    private User user =new User();

    private Teacher teacher;
    private ShowExecutedExamsController TempController;
    public void setUser(User user){this.user=user;}

    public void setTeacher(Teacher teacher){this.teacher=teacher;}

    /*the standard fxml initialize made a bug for me - i couldnt send the user
    * */
    void initializee() {
        EventBus.getDefault().register(this);
        welcomeLabel.setText("Welcome "+ user.getFullName());
    }
        // both three functions below ensure for us that teacher is updated when we still online
    @Subscribe
    public void onReceivingQuestionEvent(ReceivingQuestionEvent message){
        setUser(message.getMessage().getTeacherWhoCreate());
    }
    @Subscribe
    public void onReceivingExam(CreateExamEvent message){
        setTeacher(message.getMessage().getExam().getTeacher());
    }
    @Subscribe
    public void onReceivingExamUpdate(UpdateExamEvent message){
        setTeacher(message.getMessage().getExam().getTeacher());
    }

    @Subscribe
    public void onShareExamEvent(ShareExamEvent shareExamEvent){
        setTeacher(shareExamEvent.getShareExamMsg().getExamToShare().getTeacher());
    }

    @Subscribe
    public void onExamErrorEvent(ExamErrorMsgEvent event) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        String.format("Message: \nData: %s\nTimestamp: %s\n",
                                event.getErrorMsg(),
                                event.getTimeStamp().format(dtf))
                );
                alert.setTitle("Alert!");
                alert.setHeaderText("Message:");
                alert.show();
            });
    }

    public void AddQuestion (ActionEvent actionEvent ){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddQuestion.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            AddQuestionController controller = loader.getController();
            controller.setTeacher(teacher);
            controller.initializee();
            Stage currentStage = new Stage();
            currentStage.setTitle("Add Question SharedPlatform");
            currentStage.setScene(scene);
            controller.setStage(currentStage);
            controller.setupWindowCloseHandler();
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void create_exam(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateExam.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            CreateExamController controller = loader.getController();
            controller.setTeacher(teacher);
            controller.initializee();
            Stage currentStage = new Stage();
            currentStage.setTitle("Create a new exam");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Show_Exams(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowExams.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            ShowExamsController controller = loader.getController();
            controller.setTeacher(teacher);
            Stage currentStage = new Stage();
            currentStage.setTitle("Created Exams");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ShowExecutedExams(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowExecutedExams.fxml"));
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);
            ShowExecutedExamsController controller = loader.getController();
            TempController = controller;
            Stage currentStage = new Stage();
            currentStage.setTitle("Executed Exams");
            currentStage.setScene(scene);
            currentStage.show();
            MsgBringExecutedExams msg = new MsgBringExecutedExams("#FetchExecutedExams",teacher);
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Subscribe
    public void onReceivingExecutedExams(ReceivingExecutedExamsEvent event){
        if(event.getMessage().getRequest().equals("#FetchedSuccessfully")){
            TempController.setTeacher(teacher);
            TempController.setExecutedExams(event.getMessage().getExamSubmittionList());
        }
    }

    public void showDetails(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDetails.fxml"));
        try {
            AnchorPane newScene = loader.load();
            Stage currentStage = new Stage();
            Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
            currentStage.setTitle(user.getFullName() + " Details");
            currentStage.setScene(scene);
            StudentDetails controller = loader.getController();
            controller.setUser(user);
            controller.initializee();
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}






















