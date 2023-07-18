package il.cshaifasweng.HSTS.client;

import org.greenrobot.eventbus.Subscribe;

public class StringSubscriber {
    private String receivedMSG;
    @Subscribe
    public void handleMSG(String msg) {
        // Handle the received student list
        this.receivedMSG = msg;
    }
    public StringSubscriber() {
        this.receivedMSG = "null";
    }
    public String getReceivedMSG(){return this.receivedMSG;}
}
