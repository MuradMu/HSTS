package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgExamSubmittion;

public class ExamSubmitEvent {
    private MsgExamSubmittion message;
    public ExamSubmitEvent(MsgExamSubmittion message) {
        this.message = message;
    }

    public MsgExamSubmittion getMessage() {
        return message;
    }
}
