package il.cshaifasweng.HSTS.client;
import javafx.scene.control.Label;

import il.cshaifasweng.HSTS.entities.MsgGetGrades;
import il.cshaifasweng.HSTS.entities.Student;
import il.cshaifasweng.HSTS.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentHomePageController implements Initializable{ //pay attention, till now you didnt register your class to eventbus i think
    //we will need it.....
    private User user;
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

    public void showStats(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentStats.fxml"));
        try {
            Stage currentStage = new Stage();
            AnchorPane newScene = loader.load();
            Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
            currentStage.setTitle(user.getFullName() + " Stats");
            currentStage.setScene(scene);
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
            currentStage.setTitle(user.getFullName() + " Grades");
            currentStage.setScene(scene);
            StudentGradesController controller = loader.getController();
            NextController = controller;
            MsgGetGrades msg = new MsgGetGrades("#GetGrades", (Student) user);
            SimpleClient.getClient().sendToServer(msg);
//            controller.setStudent((Student) user);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(User user){
        this.user=user;
        nameLabel.setText(user.getFullName());
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
                controller.setStudent((Student) user);
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
                Student student = (Student) user;
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
