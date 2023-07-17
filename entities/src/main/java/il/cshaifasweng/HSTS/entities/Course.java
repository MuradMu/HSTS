package il.cshaifasweng.HSTS.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
public class Course implements Serializable {
    @Id
    @Column(name = "Course_ID")
    private String id;

    @Column(name = "Course_Name")
    private String course_name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "Username")
    )
    private List<Student> students= new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Exam> exams = new ArrayList<>();


    public Course(String id, String name, Teacher teacher) {
        super();
        this.id = id;
        this.teacher = teacher;
        if (teacher != null) {
            teacher.AddCourse(this);
        }
        this.course_name = name;
    }
    public Course() {
        super();
        this.id = "Unknown";
        this.course_name = "Unknown";
    }
    public void add_student(Student student){
        students.add(student);
    }

    public String getCourse_name(){
        return this.course_name;
    }

   // public void addQuestion(Question question){this.questions.add(question);}
  //  public List<Question> getQuestions(){return this.questions;}
    public String getId(){return this.id;}

    public List<Student> getStudents() {return this.students;}

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public void addExam(Exam exam) {
        this.exams.add(exam);
    }
    public void removeExam(Exam exam){
        this.exams.remove(exam);
    }
}
