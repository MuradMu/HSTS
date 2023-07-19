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
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditExamController implements Initializable {
    private ShowExamsController PreviousController;
    private Teacher teacher;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TextField time_minutes;
    @FXML
    private ChoiceBox<Course> courseChoiceBox;
    @FXML
    private TextField teacher_discription;
    @FXML
    private TextField student_discription;
    private List<Question> questions = new ArrayList<>();

    private List<Question> selectedQuestions = new ArrayList<>();

    private Exam exam;
    public void initializee() {
        List<Course> teacherCourses = teacher.getCourses();
        ObservableList<Course> courseList = FXCollections.observableArrayList(teacherCourses);
        courseChoiceBox.setItems(courseList);
        courseChoiceBox.setConverter(new StringConverter<Course>() {
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
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0,
                c -> c.getControlNewText().matches("\\d*") ? c : null);
        time_minutes.setTextFormatter(formatter);
        ObservableList<Question> questionsForTeacher = FXCollections.observableArrayList();
        List<Question> questionList = teacher.getTeacherQuestionsList();
        if (!questionList.isEmpty()) {
            questionsForTeacher.addAll(questionList);
            questions.addAll(questionList);
        }
        for(Question question : exam.getQuestions()){
            for(Question question1 : questions){
                if(question1.getIdNum() == question.getIdNum()){
                    question1.setSelected(true);
                    question1.setPoints(exam.getQuestionPoints().get(question));
                    selectedQuestions.add(question1);
                }
            }
        }
        courseChoiceBox.setValue(exam.getCourse());
        time_minutes.setText(Integer.toString(exam.getTime()));
        questionTable.setItems(questionsForTeacher);
        teacher_discription.setText(exam.getDescription_Teacher());
        student_discription.setText(exam.getDescription_Student());
    }

    @FXML
    public void UpdateExam(ActionEvent actionEvent) throws IOException {
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
            int time = Integer.parseInt(time_minutes.getText());
            if(time > 0){
                Course selectedCourse = courseChoiceBox.getValue();
                if (selectedCourse != null) {
                    //Create the exam and send it to the server.
                    Map<Question, Integer> questionPoints = new HashMap<>();
                    for(Question question : selectedQuestions){
                        questionPoints.put(question, question.getPoints());
                    }
                    String dis1 = teacher_discription.getText();
                    String dis2 = student_discription.getText();
                    Exam exam = new Exam(teacher,selectedCourse,selectedQuestions, time, questionPoints,dis1,dis2);
                    teacher.removeExam(exam);
                    selectedCourse.removeExam(exam);
                    MsgUpdateExam msg = new MsgUpdateExam("#EditExam", exam);
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
    public void onReceivingExamUpdate(UpdateExamEvent message){
        EventBus.getDefault().post(new ExamErrorMsgEvent("Exam Updated Successfully!"));
        Platform.runLater(() -> {
            // Get the window or stage that contains the exam creation UI
            Exam exam = message.getMessage().getExam();
           // teacher.addExam(exam);
           // exam.getCourse().addExam(exam);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            PreviousController.setTeacher(message.getMessage().getExam().getTeacher());
            PreviousController.updateLIST();

            // Close the window
            stage.close();
           // EventBus.getDefault().unregister(this);
        });
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Question, Integer> questionNumCol = new TableColumn<>("Question_num");
        TableColumn<Question, String> questionCol = new TableColumn<>("Question");
        TableColumn<Question, String> aCol = new TableColumn<>("A");
        TableColumn<Question, String> bCol = new TableColumn<>("B");
        TableColumn<Question, String> cCol = new TableColumn<>("C");
        TableColumn<Question, String> dCol = new TableColumn<>("D");
        TableColumn<Question, String> answerCol = new TableColumn<>("Answer");

        questionNumCol.setCellValueFactory(new PropertyValueFactory<>("IdNum"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        aCol.setCellValueFactory(new PropertyValueFactory<>("answerA"));
        bCol.setCellValueFactory(new PropertyValueFactory<>("answerB"));
        cCol.setCellValueFactory(new PropertyValueFactory<>("answerC"));
        dCol.setCellValueFactory(new PropertyValueFactory<>("answerD"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        TableColumn<Question, Boolean> selectCol = new TableColumn<>("Select");
        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectCol.setCellFactory(column -> new TableCell<Question, Boolean>() {
            private final Button selectButton = new Button();

            {
                selectButton.setOnAction(event -> {
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
                        setGraphic(selectButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private void updateButtonState(boolean selected) {
                if (selected) {
                    selectButton.setText("Deselect");
                } else {
                    selectButton.setText("Select");
                }
            }
        });

        TableColumn<Question, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        pointsColumn.setCellFactory(column -> {
            return new TableCell<Question, Integer>() {
                private TextField textField = new TextField();

                {
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*")) {
                            textField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    });
                    textField.setOnAction(event -> {
                        int points = Integer.parseInt(textField.getText());
                        // Handle the points input as desired, e.g., store it in the Question object
                        Question question = getTableRow().getItem();
                        question.setPoints(points);
                    });

                    textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            int points = Integer.parseInt(textField.getText());
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
                        setGraphic(textField);
                        if (isEditing()) {
                            textField.setText(getString());
                        } else {
                            textField.setText(String.valueOf(item));
                        }
                    }
                }

                private String getString() {
                    return getItem() == null ? "" : getItem().toString();
                }
            };
        });
        questionTable.getColumns().addAll(selectCol, pointsColumn,questionNumCol, questionCol, aCol, bCol, cCol, dCol, answerCol);
        EventBus.getDefault().register(this);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setPreviousController(ShowExamsController previousController) {
        PreviousController = previousController;
    }
}
