package il.cshaifasweng.HSTS.client;
import java.time.LocalDateTime;
public class ExamErrorMsgEvent {
    private String errorMsg;
    LocalDateTime timeStamp;


    public ExamErrorMsgEvent(String errorMsg) {
        this.errorMsg = errorMsg;
        this.timeStamp = LocalDateTime.now();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
