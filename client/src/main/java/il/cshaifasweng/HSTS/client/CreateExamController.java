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
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class CreateExamController implements Initializable{
    private Teacher teacher;

    @FXML
    private Button AddExamButton;

    @FXML
    private ChoiceBox<Course> ChooseCourseBox;

    @FXML
    private TextField ExamTime;

    @FXML
    private TableView<Question> QuestionTable;

    @FXML
    private TextField StudentDiscription;

    @FXML
    private TextField TeacherDiscription;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label student_disc;

    @FXML
    private Label teacher_disc;
    private List<Question> questions = new ArrayList<>();
    public void updateLIST() {
        QuestionTable.refresh();
    }

    private List<Question> selectedQuestions = new ArrayList<>();
    public void initializee() {
        List<Course> teacherCourses = teacher.getCourses();
        ObservableList<Course> courseList = FXCollections.observableArrayList(teacherCourses);
        ChooseCourseBox.setItems(courseList);
        ChooseCourseBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                if (course == null) {
                    return "";
                }
                return course.getCourse_name();
            }
            @Override
            public Course fromString(String string) {
                // Not used in this case
                return null;
            }
        });
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0, c -> c.getControlNewText().matches("\\d*") ? c : null);ExamTime.setTextFormatter(formatter);

        ObservableList<Question> questionsForTeacher = FXCollections.observableArrayList();

        List<Question> questionList = teacher.getTeacherQuestionsList();
        if (!questionList.isEmpty()) {
            questionsForTeacher.addAll(questionList);
            questions.addAll(questionList);
        }
        for(Question question : questionsForTeacher){
            question.setPoints(0);
            question.setSelected(false);
        }
        QuestionTable.setItems(questionsForTeacher);
    }
    @FXML
    public void AddExam(ActionEvent actionEvent) throws IOException {
        Map<Question, Integer> question_grade = new HashMap<>();
        int sum=0;
        int points;
        boolean flag = true;
        if(selectedQuestions.isEmpty()){
            EventBus.getDefault().post(new ErrorMsgEvent("No Questions were selected!"));
            flag = false;
        }else{
            for(Question question : selectedQuestions){
                if(question.isSelected()){
                    points = question.getPoints();
                    if(points == 0){
                        flag = false;
                    }else{
                        sum += question.getPoints();
                    }
                }
            }
            if(flag){
                if(sum!=100){
                    EventBus.getDefault().post(new ErrorMsgEvent("POINTS SUM IS NOT 100!"));
                    flag = false;
                }
            }else{
                EventBus.getDefault().post(new ErrorMsgEvent("Selected Questions with points of 0!"));
            }
        }
        if(flag){
            int time = Integer.parseInt(ExamTime.getText());
            if(time > 0){
                Course selectedCourse = ChooseCourseBox.getValue();
                if (selectedCourse != null) {
                    //Create the exam and send it to the server.
                    Map<Question, Integer> questionPoints = new HashMap<>();
                    for(Question question : selectedQuestions){
                        questionPoints.put(question, question.getPoints());
                    }
                    String dis1 = TeacherDiscription.getText();
                    String dis2 = StudentDiscription.getText();
                    Exam exam = new Exam(teacher,selectedCourse,selectedQuestions, time, questionPoints,dis1,dis2);
                    teacher.removeExam(exam);
                    selectedCourse.removeExam(exam);
                    MsgExamCreation msg = new MsgExamCreation("#NewExam", exam);
                    SimpleClient.getClient().sendToServer(msg);
                } else {
                    EventBus.getDefault().post(new ExamErrorMsgEvent("No course is selected!"));
                }
            }else{
                EventBus.getDefault().post(new ExamErrorMsgEvent("Time is not set correctly!"));
            }
        }
    }
    @Subscribe
    public void onReceivingExam(CreateExamEvent message){
        if(message.getMessage().getRequest().equals("#ExamCreationDone")){
            EventBus.getDefault().post(new ExamErrorMsgEvent("Exam Created Successfully!"));
            EventBus.getDefault().unregister(this);
            Platform.runLater(() -> {
                // Get the window or stage that contains the exam creation UI
                Exam exam = message.getMessage().getExam();
                //teacher.addExam(exam);
                //exam.getCourse().addExam(exam);
                Stage stage = (Stage) rootPane.getScene().getWindow();

                // Close the window
                stage.close();
            });
        }
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> Number_of_Question_Colum = new TableColumn<>("Number of Question");
        TableColumn<Question, String> Question_Text_Colum = new TableColumn<>("Question Text");
        TableColumn<Question, String> Answer_A_Colum = new TableColumn<>("Answer A");
        TableColumn<Question, String> Answer_B_Colum = new TableColumn<>("Answer B");
        TableColumn<Question, String> Answer_C_Colum = new TableColumn<>("Answer C");
        TableColumn<Question, String> Answer_D_Colum = new TableColumn<>("Answer D");
        TableColumn<Question, String> Correct_Answer_Colum = new TableColumn<>("Correct Answer");

        Number_of_Question_Colum.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        Question_Text_Colum.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        Answer_A_Colum.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        Answer_B_Colum.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        Answer_C_Colum.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        Answer_D_Colum.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        Correct_Answer_Colum.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        TableColumn<Question, Boolean> Select_Colum = new TableColumn<>("Select");
        Select_Colum.setCellValueFactory(new PropertyValueFactory<>("selected"));
        Select_Colum.setCellFactory(column -> new TableCell<Question, Boolean>() {
            private final Button SelectButton = new Button();

            {
                SelectButton.setOnAction(event -> {
                    Question question = getTableRow().getItem();
                    boolean selected = !question.isSelected();
                    if (selected) {
                        selectedQuestions.add(question);
                    } else {
                        selectedQuestions.remove(question);
                    }
                    question.setSelected(selected);
                    updateButtonState(selected);
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Question question = getTableRow().getItem();
                    if (question != null) {
                        updateButtonState(question.isSelected());
                        setGraphic(SelectButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private void updateButtonState(boolean selected) {
                if (selected) {
                    SelectButton.setText("Deselect");
                } else {
                    SelectButton.setText("Select");
                }
            }
        });

        TableColumn<Question, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        pointsColumn.setCellFactory(column -> {
            return new TableCell<Question, Integer>() {
                private TextField NewTextField = new TextField();

                {
                    NewTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*")) {
                            NewTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    });
                    NewTextField.setOnAction(event -> {
                        int points = Integer.parseInt(NewTextField.getText());
                        // Handle the points input as desired, e.g., store it in the Question object
                        Question question = getTableRow().getItem();
                        question.setPoints(points);
                    });

                    NewTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            int points = Integer.parseInt(NewTextField.getText());
                            // Handle the points input as desired, e.g., store it in the Question object
                            Question question = getTableRow().getItem();
                            question.setPoints(points);
                        }
                    });
                }

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(NewTextField);
                        if (isEditing()) {
                            NewTextField.setText(getString());
                        } else {
                            NewTextField.setText(String.valueOf(item));
                        }
                    }
                }

                private String getString() {
                    return getItem() == null ? "" : getItem().toString();
                }
            };
        });


        QuestionTable.getColumns().addAll(Select_Colum, pointsColumn);
        QuestionTable.getColumns().addAll(Number_of_Question_Colum, Question_Text_Colum, Answer_A_Colum, Answer_B_Colum, Answer_C_Colum, Answer_D_Colum, Correct_Answer_Colum);
        EventBus.getDefault().register(this);
    }
}
