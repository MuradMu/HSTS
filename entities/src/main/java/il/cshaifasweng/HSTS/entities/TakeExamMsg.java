package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;

public class TakeExamMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private String request = "";

    private String ExamIdNum;
    private Exam ExamToShare;
    private String PasswordToSet;

    private Course course;
    private Student student;

    public TakeExamMsg(String request, String ExamIdNum, String PasswordToSet, Course course, Student student) {
        this.request = request;
        this.ExamIdNum = ExamIdNum;
        this.PasswordToSet = PasswordToSet;
        this.course = course;
        this.student = student;
    }

    public TakeExamMsg(String request, Exam examToShare) {
        this.request = request;
        this.ExamToShare = examToShare;
    }


    public String getExamIdNum() {
        return ExamIdNum;
    }

    public void setExamIdNum(String examIdNum) {
        ExamIdNum = examIdNum;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Exam getExamToShare() {
        return ExamToShare;
    }


    public void setExamToShare(Exam examToShare) {
        ExamToShare = examToShare;
    }


    public String getPasswordToSet() {
        return PasswordToSet;
    }

    public void setPasswordToSet(String passwordToSet) {
        PasswordToSet = passwordToSet;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
