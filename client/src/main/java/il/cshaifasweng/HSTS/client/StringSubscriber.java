package il.cshaifasweng.HSTS.client;

import org.greenrobot.eventbus.Subscribe;
public class StringSubscriber {
    private String msg;
    @Subscribe
    public void handleMSG(String msg) {
        // Handle the received student list
        this.msg = msg;
    }
    public StringSubscriber() {
        this.msg = "";
    }
    public String getReceivedMSG(){return this.msg;}
}
