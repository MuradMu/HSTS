package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;
import java.util.List;

public class MsgBringExecutedExams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String request;

    private List<ExamSubmittion> examSubmittionList;
    private Teacher teacher;
    public MsgBringExecutedExams(String request, List<ExamSubmittion> examSubmittionList){
        this.request = request;
        this.examSubmittionList = examSubmittionList;
    }
    public MsgBringExecutedExams(String request, Teacher teacher){
        this.request = request;
        this.teacher = teacher;
    }
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public List<ExamSubmittion> getExamSubmittionList() {
        return examSubmittionList;
    }

    public void setExamSubmittionList(List<ExamSubmittion> examSubmittionList) {
        this.examSubmittionList = examSubmittionList;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
