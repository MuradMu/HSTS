package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Question;
import il.cshaifasweng.HSTS.entities.QuestionMsg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditQuestionController {
    AddQuestionController addQuestionController;
    private Question question;
    @FXML
    private TextField questiontext;

    @FXML
    private TextField Answer_A;

    @FXML
    private TextField Answer_B;

    @FXML
    private TextField Answer_C;

    @FXML
    private TextField Answer_D;

    @FXML
    private ChoiceBox CorrectanswerBox;

    public void initializee(){
        questiontext.setText(question.getQuestionText());
        Answer_A.setText(question.getAnswerA());
        Answer_B.setText(question.getAnswerB());
        Answer_C.setText(question.getAnswerC());
        Answer_D.setText(question.getAnswerD());
        CorrectanswerBox.setValue(question.getCorrectAnswer());
    }
    public void EditQuestion(ActionEvent actionEvent) throws IOException {
        List<String> ListOfAnswers = new ArrayList<>();
        String QuestionText,CorrectAnswer;
        QuestionText= questiontext.getText();
        ListOfAnswers.add(Answer_A.getText());
        ListOfAnswers.add(Answer_B.getText());
        ListOfAnswers.add(Answer_C.getText());
        ListOfAnswers.add(Answer_D.getText());
        CorrectAnswer= (String) CorrectanswerBox.getValue();
        Question Edited = new Question (QuestionText,ListOfAnswers,CorrectAnswer, addQuestionController.getTeacher());
        QuestionMsg msg = new QuestionMsg("#AddQuestion", Edited, addQuestionController.getTeacher());
        SimpleClient.getClient().sendToServer(msg);
        addQuestionController.updateLIST();
        // now wee need to close the scene
        Node sourceNode = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();

    }
    public void setPreviousLoader(AddQuestionController HController){this.addQuestionController = HController;}
    public Question getQuestion() {return this.question;}
    public void setQuestion(Question question){this.question = question;}
}
