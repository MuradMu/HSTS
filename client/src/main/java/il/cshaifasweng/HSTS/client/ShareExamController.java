package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.ShareExamMsg;
import il.cshaifasweng.HSTS.entities.Teacher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShareExamController implements Initializable {
    @FXML
    private Button DoneButton;

    @FXML
    private TextArea InformationToShare;

    @FXML
    private Button SetButton;
    @FXML
    private TextField DigitsText;
    private ShowExamController showExamController;
    //private ShowExamController showExamsController;

    private String Password;

    private Teacher teacher;
    private Exam exam;


    public void OnSetButton(ActionEvent actionEvent){
        if (DigitsText.getText().length()!=4) {
            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Message: \nData: %s",
                            "Set 4 digits for the Password "));
            alert.setTitle("Alert!");
            alert.setHeaderText("Message:");
            alert.show();
            });
        } else {
            Password=DigitsText.getText();
            InformationToShare.setText("Exam id :"+String.valueOf(exam.getId_num())+"\n"+
                    "Password: "+Password );
        }
    }


    public void OnDoneButton(ActionEvent actionEvent){
        ShareExamMsg msg = new ShareExamMsg(exam,Password);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            System.out.print(" cannot send msg to server from ShareExamController");
            throw new RuntimeException(e);
        }
        Node sourceNode = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onShareExamEvent(ShareExamEvent shareExamEvent){
        showExamController.setExam(shareExamEvent.getShareExamMsg().getExamToShare());
        Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Message: \nData: %s",
                            "Exam Shared successfully  "));
            alert.setTitle("Alert!");
            alert.setHeaderText("Message:");
            alert.show();
        });

    }

    public void setPreviousLoader(ShowExamController showExamController){
        this.showExamController =showExamController;}

    /*public void setShowExamsLoader(ShowExamsController showExamController){
        this.showExamsController =showExamsController;}*/

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        InformationToShare.setEditable(false);
    }
}
