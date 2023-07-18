package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Course;
import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.Student;
import il.cshaifasweng.HSTS.entities.TakeExamMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentTakeExamController implements Initializable {
    private Student student;
    private Exam exam;

    @FXML
    private TextField DigitsText;
    @FXML
    private TextField IdNumText;

    private boolean takeExamFlag = true;
    private StudentHomePageController Pcontroller;

    public void setPcontroller(StudentHomePageController pcontroller) {
        this.Pcontroller = pcontroller;
        Stage stage = (Stage) IdNumText.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            unregisterFromEventBus();
            TurnFlagOff();
        });
    }

    private void TurnFlagOff() {
        Pcontroller.setTake_exam(false);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setStudent(Student student) {
        this.student = student;
        List<Course> studentCourses = student.getCourses();
        ObservableList<Course> courseList = FXCollections.observableArrayList(studentCourses);
//        courseChoiceBox.setItems(courseList);
//        courseChoiceBox.setConverter(new StringConverter<Course>() {
//            @Override
//            public String toString(Course course) {
//                if (course == null) {
//                    return "";
//                }
//                return course.getCourse_name();
//            }
//
//            @Override
//            public Course fromString(String string) {
//                // Not used in this case
//                return null;
//            }
//        });
    }

    public Exam getExam() {
        return exam;
    }

    public Student getStudent() {
        return student;
    }
//    @FXML
//    private ChoiceBox<Course> courseChoiceBox;
    public void TakeExam(ActionEvent actionEvent) {
        String password = DigitsText.getText();
        String ExamIdNum=IdNumText.getText();
        if(password.length()!=4){
            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        String.format("Message: \nData: %s",
                                "Password length error!"));
                alert.setTitle("Alert!");
                alert.setHeaderText("Message:");
                alert.show();
            });
        }else{
//            Course selectedCourse = courseChoiceBox.getValue();
            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                TakeExamMsg msg = new TakeExamMsg("TakeExamRequest",ExamIdNum ,password, null, student);
                try {
                    SimpleClient.getClient().sendToServer(msg);
                } catch (IOException e) {
                    System.out.print("cannot send msg to server from StudentTakeExamController");
                    throw new RuntimeException(e);
                }
            });
        }
    }
    @Subscribe
    public void onTakeStartEvent(StartExamEvent startExamEvent){
        if(takeExamFlag){
//            Pcontroller.setTake_exam(false);
            TakeExamMsg msg = startExamEvent.getTakeExamMsg();
            if(msg.getRequest().equals("#ExamReturnedSuccessfully")){//Exam is returned successfully, The student canstart taking it now
                Platform.runLater(() -> {
                    EventBus.getDefault().unregister(this);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentExamPage.fxml"));
                        AnchorPane newScene = loader.load();
                        Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
                        Stage currentStage = (Stage) IdNumText.getScene().getWindow();
                        currentStage.setTitle("Exam");
                        currentStage.setScene(scene);
                        StudentExamPageController controller = loader.getController();
                        controller.setPPcontrolelr(Pcontroller);
                        controller.setParameters(student, msg.getExamToShare());
                        currentStage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }else if(msg.getRequest().equals("#ExamReturnedUnsuccessfully")){////Exam is returned unsuccessfully
                Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            String.format("Message: \nData: %s",
                                    "Wrong Exam Details!"));
                    alert.setTitle("Alert!");
                    alert.setHeaderText("Message:");
                    alert.show();
                });
            }else if(msg.getRequest().equals("#StudentAlreadyTookExam")){
                Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            String.format("Message: \nData: %s",
                                    "Exam Already Executed!"));
                    alert.setTitle("Alert!");
                    alert.setHeaderText("Message:");
                    alert.show();
                });
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
    public void unregisterFromEventBus() {
        System.out.print("Unregistering");
        EventBus.getDefault().unregister(this);
    }
}