package il.cshaifasweng.HSTS.client;
import il.cshaifasweng.HSTS.entities.*;
import javafx.scene.control.Label;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentHomePageController implements Initializable{ //pay attention, till now you didnt register your class to eventbus i think
    //we will need it.....
    private Student student;
    private StudentGradesController NextController;
    @FXML
    private Label nameLabel;

    private boolean take_exam = false;


    public void showDetails(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDetails.fxml"));
        try {
            AnchorPane newScene = loader.load();
            Stage currentStage = new Stage();
            Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
            currentStage.setTitle(student.getFullName() + " Details");
            currentStage.setScene(scene);
            StudentDetails controller = loader.getController();
            controller.setUser(student);
            controller.initializee();
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showGrades(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentGrades.fxml"));
        try {
            Stage currentStage = new Stage();
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
            currentStage.setTitle(student.getFullName() + " Grades");
            currentStage.setScene(scene);
            StudentGradesController controller = loader.getController();
            NextController = controller;
            MsgGetGrades msg = new MsgGetGrades("#GetGrades", (Student) student);
            SimpleClient.getClient().sendToServer(msg);
//            controller.setStudent((Student) user);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(Student user){
        this.student =user;
        nameLabel.setText(user.getFullName() + " Home Page, Welcome!");
    }

    public void TakeExam(ActionEvent actionEvent) {
        if(!take_exam){
            take_exam = true;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentTakeExam.fxml"));
            try {
                Stage currentStage = new Stage();
                AnchorPane newScene = loader.load();
                Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
                currentStage.setTitle("Take Exam");
                currentStage.setScene(scene);
                StudentTakeExamController controller = loader.getController();
                controller.setStudent((Student) student);
                controller.setPcontroller(this);
                currentStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Subscribe
    public void onReceivingGrades(GradesRecievedEvent message){
        if(message.getMessage().getRequest().equals("#GradesReturned")){
            Platform.runLater(() -> {
                Student student = (Student) this.student;
                student.setGrades(message.getMessage().getGradeList());
                NextController.setStudent(student);
            });
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        EventBus.getDefault().register(this);
    }

    public void setTake_exam(boolean take_exam) {
        this.take_exam = take_exam;
    }
}
