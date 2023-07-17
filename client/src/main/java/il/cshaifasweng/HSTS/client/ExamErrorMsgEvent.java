package il.cshaifasweng.HSTS.client;
import java.time.LocalDateTime;
public class ExamErrorMsgEvent {
    private String msg;
    LocalDateTime L_time;


    public ExamErrorMsgEvent(String msg) {
        this.msg = msg;
        this.L_time = LocalDateTime.now();
    }

    public String getmsg() {
        return this.msg;
    }

    public LocalDateTime getLTime() {
        return L_time;
    }
}
