package il.cshaifasweng.HSTS.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
//@Table(name = "Teachers")
public class Teacher extends User implements Serializable {
  /*  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_num;*/  // no need for id because its son class . user already have

    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Question> TeacherQuestionsList  = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Exam> exams = new ArrayList<>();
    public Teacher(String id, String first, String last, String username, String role, String mail, String password){
        super(id, first, last, username, role, mail,password);
       // this.TeacherQuestionsList=new ArrayList<>();
    }

    public Teacher() {
        super();
    }

    public void AddCourse(Course course){this.courses.add(course);}
    public void SetCourses(List<Course> courses){this.courses=courses;}

    public List<Question> getTeacherQuestionsList() {
        return TeacherQuestionsList;
    }

    public void setTeacherQuestionsList(List<Question> teacherQuestionsList) {
        TeacherQuestionsList = teacherQuestionsList;
    }
    public void AddQuestion(Question question){
        this.TeacherQuestionsList.add(question);
    }

    public List<Course> getCourses(){return this.courses;}
    public void addQuestion(Question question){this.TeacherQuestionsList.add(question);}
    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public void addExam(Exam exam){
        this.exams.add(exam);
    }

    public void setTeacherExamsList(List<Exam> exams) {
        this.exams = exams;
    }

    public void removeExam(Exam exam){
        this.exams.remove(exam);
    }
}
