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
import java.util.ResourceBundle;

public class TShowQueController {
    private Stage stage;
    private Teacher teacher;
    private TableColumn<Question, Void> editCol;
    @FXML
    private TableView<Question> QuesTable;

    public void updateLIST() {
        QuesTable.refresh();
    }

    public void initializee() {
        ObservableList<Question> TeacherQuestions= FXCollections.observableArrayList();
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

        QuesTable.getColumns().addAll(
                questionNumCol, questionCol, aCol, bCol, cCol, dCol, answerCol
        );

        List<Question> questionList = teacher.getTeacherQuestionsList();// todo check
        if(questionList.isEmpty()){
//            System.out.print("\nSystem check Q.list is empty : ");
        }else {
            for(Question question : questionList){
//                {System.out.print("\nSystem check for Q.list: " + question.getQuestionText() + "\n");}
                TeacherQuestions.add(question);
            }
        }

        QuesTable.setItems(TeacherQuestions);// this should show the questions


    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    //  @Override
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

