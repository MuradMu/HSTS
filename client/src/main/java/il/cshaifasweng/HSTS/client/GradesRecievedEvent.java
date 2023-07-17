package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgGetGrades;

public class GradesRecievedEvent {
    private MsgGetGrades message;
    public GradesRecievedEvent(MsgGetGrades message) {
        this.message = message;
    }

    public MsgGetGrades getMessage() {
        return message;
    }
}
