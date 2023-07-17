package il.cshaifasweng.HSTS.entities;
import java.io.Serializable;

public class ShareExamMsg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String request="";
    private Exam ExamToShare;
    private String PasswordToSet;


    public ShareExamMsg(Exam examToShare, String passwordToSet) {
        ExamToShare = examToShare;
        PasswordToSet = passwordToSet;
    }

    public ShareExamMsg(String request, Exam examToShare, String passwordToSet) {
        this.request = request;
        ExamToShare = examToShare;
        PasswordToSet = passwordToSet;
    }

    public ShareExamMsg(String request, Exam examToShare) {
        this.request = request;
        ExamToShare = examToShare;
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
}

