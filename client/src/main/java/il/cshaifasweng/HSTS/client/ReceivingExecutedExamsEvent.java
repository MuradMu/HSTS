package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgBringExecutedExams;

public class ReceivingExecutedExamsEvent {
    private MsgBringExecutedExams message;

    public  MsgBringExecutedExams  getMessage() {
        return message;
    }

    public ReceivingExecutedExamsEvent(MsgBringExecutedExams message) {
        this.message = message;
    }
}
