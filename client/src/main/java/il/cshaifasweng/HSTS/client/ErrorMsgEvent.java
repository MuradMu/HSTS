package il.cshaifasweng.HSTS.client;

import java.time.LocalDateTime;

public class ErrorMsgEvent {
    private String errorMsg;
    LocalDateTime timeStamp;


    public ErrorMsgEvent(String errorMsg) {
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
