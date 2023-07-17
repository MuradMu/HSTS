package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.QuestionMsg;

public class ReceivingQuestionEvent {
    private QuestionMsg message;

    public  QuestionMsg  getMessage() {
        return message;
    }

    public ReceivingQuestionEvent(QuestionMsg message) {
        this.message = message;
    }
}
