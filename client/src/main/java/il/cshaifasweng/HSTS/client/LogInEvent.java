package il.cshaifasweng.HSTS.client;
import il.cshaifasweng.HSTS.entities.MsgToLogIn;

public class LogInEvent {

    private MsgToLogIn message;

    public  MsgToLogIn  getMessage() {
        return message;
    }

    public LogInEvent(MsgToLogIn message) {
        this.message = message;
    }
}