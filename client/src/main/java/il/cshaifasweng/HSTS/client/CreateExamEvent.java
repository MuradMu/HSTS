package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgExamCreation;

public class CreateExamEvent {
    private MsgExamCreation message;

    public MsgExamCreation getMessage() {
        return message;
    }

    public CreateExamEvent(MsgExamCreation message) {
        this.message = message;
    }
}
