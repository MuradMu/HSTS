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
//@Table(name = "Students")
public class Student extends User implements Serializable {
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_num;*/ //no need for id because its son class . user already have

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", fetch = FetchType.EAGER)
    private List<Grade> grades = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", fetch = FetchType.EAGER)
    @MapKeyJoinColumn(name = "Course_ID")
    private Map<Course, Grade> gradesMap = new HashMap<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ExamSubmittion> submitted_exams = new ArrayList<>();
    public Student(String id, String first, String last, String username, String role, String mail, String password){
        super(id, first, last, username, role, mail, password);
    }

    public Student() {
        super("Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown");
    }
    public void add_course(Course course){
        courses.add(course);
        gradesMap.put(course , null);
        course.add_student(this);
    }

    public void set_courseGradee(Course course, Grade grade){
        gradesMap.put(course , grade);
        grades.add(grade);
    }

    public int get_studentGrade(Course course){
        return gradesMap.get(course).getGrade();
    }

    public List<Integer> get_studentGrades(){
        List<Integer> s = new ArrayList<>();
        for(Course course : courses){
            s.add(gradesMap.get(course).getGrade());
        }
        return s;
    }

    public String getStudentName(){
        return getFirst() + " " + getLast();
    }
    public Map<String, Integer> getGradesMap(){
        Map<String, Integer> map = new HashMap<>();
        for (Course course : courses){
            map.put(course.getCourse_name(), gradesMap.get(course).getGrade());
        }
        return map;
    }

    public void updateGrade(Course course, int new_grade){
        Grade grade = gradesMap.get(course);
        grade.updateGrade(new_grade);
    }

    public List<Grade>  getGrades(){
        return this.grades;
    }

    //public int getNumID(){return this.id_num;}
//    public String getID(){return this.id;}
//    public String getFirstName(){return this.first;}
//    public String getLastName(){return this.last;}
    public List<Course> getCourses(){return this.courses;}

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
