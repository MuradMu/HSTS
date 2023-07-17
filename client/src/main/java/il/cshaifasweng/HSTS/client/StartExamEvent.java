package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.TakeExamMsg;

public class StartExamEvent {
    private TakeExamMsg takeExamMsg;
    public StartExamEvent(TakeExamMsg takeExamMsg){
        this.takeExamMsg = takeExamMsg;
    }
    public TakeExamMsg getTakeExamMsg() {
        return takeExamMsg;
    }

    public void setTakeExamMsg(TakeExamMsg takeExamMsg) {
        this.takeExamMsg = takeExamMsg;
    }
}
