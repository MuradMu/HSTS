package il.cshaifasweng.HSTS.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Exams")
public class Exam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_num;

    @Column(name = "Time")
    int time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "Exam_Question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Question_Points",
            joinColumns = {@JoinColumn(name = "exam_id", referencedColumnName = "id_num")})
    @MapKeyJoinColumn(name = "question_id")
    @Column(name = "points")
    private Map<Question, Integer> questionPoints = new HashMap<>();

   @Column(name="password",length = 4)
    private String password="";

    @Column(name="IsShared",length = 4)
    private Boolean IsShared= false;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ExamSubmittion> submitted_exams = new ArrayList<>();


    public Exam() {
        // Default constructor
    }



    public Exam(Teacher teacher, Course course, List<Question> questions, int time, Map<Question, Integer> questionPoints){
        this.course = course;
        this.teacher = teacher;
        this.questions = questions;
        this.time = time;
        this.questionPoints = questionPoints;
        this.password="";
        this.IsShared=false;
        course.addExam(this);
        teacher.addExam(this);
        for(Question question:questions){
            question.addExam(this);
        }
    }


    public Boolean getShared() {
        return IsShared;
    }


    public void setShared(Boolean IsShared) {
        this.IsShared = IsShared;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getId_num() {
        return id_num;
    }

    public int getTime() {
        return time;
    }

    public Map<Question, Integer> getQuestionPoints() {
        return questionPoints;
    }


}
