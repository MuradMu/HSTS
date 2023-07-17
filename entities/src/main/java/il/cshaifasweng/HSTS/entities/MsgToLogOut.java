package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;

public class MsgToLogOut  implements Serializable {
    private static final long serialVersionUID = 1L;
    String request;
    private User user = null;

    public MsgToLogOut(String request, User user) {
        this.request = request;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }
}
