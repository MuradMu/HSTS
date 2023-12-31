package il.cshaifasweng.HSTS.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Grades")
public class Grade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Student_name")
    private String student_name;

    @Column(name = "Course_name")
    private String course_name;

    @Column(name = "Grade")
    private int grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private ExamSubmittion exam;



    public Grade(int g, Student student, Course course, ExamSubmittion exam){
        super();
        this.grade = g;
        this.course = course;
        this.student = student;
        this.exam = exam;
        this.course_name = course.getCourse_name();
        this.student_name = student.getStudentName();
        student.set_courseGradee(course, this);
        System.out.println("this exam " + this.exam.getId_num());
    }
    public Grade() {
        this.grade = -1;
    }
    public int getGrade(){return this.grade;}

    public void setExam(ExamSubmittion exam) {
        this.exam = exam;
    }
    public ExamSubmittion getExam(){return this.exam;}
    public void updateGrade(int new_Grade){
        this.grade = new_Grade;
    }

    public String getCourse_name(){return this.course_name;}

    public int getId(){return this.id;}

    public Course getCourse() {
        return course;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
