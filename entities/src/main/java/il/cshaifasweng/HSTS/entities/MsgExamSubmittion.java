package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;

public class MsgExamSubmittion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String request;

    private ExamSubmittion exam;
    public MsgExamSubmittion(String request, ExamSubmittion exam){
        this.request = request;
        this.exam = exam;
    }

    public String getRequest() {
        return request;
    }

    public ExamSubmittion getExam() {
        return exam;
    }

    public void setExam(ExamSubmittion exam) {
        this.exam = exam;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
