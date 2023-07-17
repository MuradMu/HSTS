package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;
import java.util.List;

public class MsgGetGrades implements Serializable {
    private static final long serialVersionUID = 1L;
    String request;
    private List<Grade> gradeList;
    private Student student;
    public MsgGetGrades(String request,Student student){
        this.request = request;
        this.student = student;
    }
    public MsgGetGrades(String request, List<Grade> gradeList){
        this.request = request;
        this.gradeList = gradeList;
    }

    public String getRequest() {
        return request;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public Student getStudent() {
        return student;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}
