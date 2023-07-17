package il.cshaifasweng.HSTS.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Entity
@Table(name = "ExamsSubmit")
public class ExamSubmittion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_num;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exam_submission_answers", joinColumns = @JoinColumn(name = "exam_submission_id"))
    @MapKeyJoinColumn(name = "question_id")
    @Column(name = "answer")
    private Map<Question, String> answers;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exam_submission_points", joinColumns = @JoinColumn(name = "exam_submission_id"))
    @MapKeyJoinColumn(name = "question_id")
    @Column(name = "points")
    private Map<Question, Integer> questionPoints = new HashMap<>();
    @Column(name = "Is_Checked")
    private boolean isChecked;
    public ExamSubmittion(Student student, Exam exam, Map<Question, String> answers){
        this.student = student;
        this.exam = exam;
        this.answers = answers;
        this.isChecked = false;
        for(Question question : exam.getQuestions()){
            this.questionPoints.put(question, 0);
        }
    }
    public ExamSubmittion(){
    }
    public Student getStudent() {
        return student;
    }

    public Exam getExam() {
        return exam;
    }

    public Map<Question, String> getAnswers() {
        return answers;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setAnswers(Map<Question, String> answers) {
        this.answers = answers;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId_num() {
        return id_num;
    }
    public boolean getChecked(){return this.isChecked;}

    public void setQuestionPoints(Map<Question, Integer> questionPoints) {
        this.questionPoints = questionPoints;
    }

    public Map<Question, Integer> getQuestionPoints() {
        return questionPoints;
    }
    public void addPoints(Question question, Integer grade){
        questionPoints.put(question,grade);
    }
}