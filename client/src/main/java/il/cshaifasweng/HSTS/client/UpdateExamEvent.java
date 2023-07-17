package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgUpdateExam;

public class UpdateExamEvent {
    private MsgUpdateExam message;

    public MsgUpdateExam getMessage() {
        return message;
    }

    public UpdateExamEvent(MsgUpdateExam message) {
        this.message = message;
    }
}
