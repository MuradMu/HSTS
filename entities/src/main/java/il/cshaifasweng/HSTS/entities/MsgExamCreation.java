package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;

public class MsgExamCreation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String request;

    private Exam exam;

    public MsgExamCreation(String request, Exam exam){
        this.request = request;
        this.exam = exam;
    }
    public Exam getExam() {
        return exam;
    }

    public String getRequest() {
        return request;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
